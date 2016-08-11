/*
  State provies a functional approach to application state.  It is effectively a function: S => (S, A) where S is your state, and A is your results. (data class)
 */

// Example - Pseudorandom numbers

case class Robot(id : Long, sentient : Boolean, name : String, model : String)

val rng = new scala.util.Random(0L)

def createRobot() = {
  val id = rng.nextLong()
  val sentient = rng.nextBoolean()
  val isJoe = rng.nextBoolean()
  val name = if (isJoe) "Joe" else "Jason"
  val isReplicant = rng.nextBoolean()
  val model = if (isReplicant) "replicant" else "borg"
  Robot(id, sentient, name, model)
}

createRobot()
createRobot()
createRobot()
createRobot()


// refactored createRobot()
def createRobot2() = {
  val id = rng.nextLong()
  val b = rng.nextBoolean()
  val sentient = b
  val isJoe = b
  val isReplicant = b
  val name = if (isJoe) "Joe" else "Jason"
  val model = if (isReplicant) "replicant" else "borg"
  Robot(id, sentient, name, model)
}

createRobot2()
createRobot2()
createRobot2()

// purely functional pseudorandom values

// Knuth's 64-bit linear congruential generator
case class Seed(long : Long) {
  def next = Seed(long * 6364136223846793005L + 1442695040888963407L)
}

def nextBoolean(seed : Seed) : (Seed, Boolean) = (seed.next, seed.long >= 0L)

def nextLong(seed : Seed) : (Seed, Long) = (seed.next, seed.long)

def createRobot3(seed: Seed): Robot = {
  val (seed1, id) = nextLong(seed)
  val (seed2, sentient) = nextBoolean(seed1)
  val (seed3, isJoe) = nextBoolean(seed2)
  val name = if (isJoe) "Joe" else "Jason"
  val (seed4, isReplicant) = nextBoolean(seed3)
  val model = if (isReplicant) "replicant" else "borg"
  Robot(id, sentient, name, model)
}

val initialSeed = Seed(13L)

createRobot3(initialSeed)
createRobot3(initialSeed)
createRobot3(initialSeed)

val nextSeed = Seed(33L)
createRobot3(nextSeed)

// cleaned up with State
import cats.data.State

val nextLong : State[Seed, Long] = State(seed => (seed.next, seed.long))

val nextBoolean : State[Seed, Boolean] = nextLong.map(_ > 0)

// map/flatmap on State allow us to use for comprehensions..*[]: 

val createRobot4 : State[Seed, Robot] =
  for {
    id <- nextLong
    sentient <- nextBoolean
    isJoe <- nextBoolean
    name = if (isJoe) "Joe" else "Jason"
    isReplicant <- nextBoolean
    model = if (isReplicant) "replicant" else "borg"
  } yield Robot(id, sentient, name, model)

val (finalState, robot) = createRobot4.run(initialSeed).value

createRobot4.run(finalState).value

// if we only care about the Robot...
createRobot4.runA(initialSeed).value

// back to the refactor...

val createRobot5 : State[Seed, Robot] = {
  val b = nextBoolean

  for {
    id <- nextLong
    sentient <- b
    isJoe <- b
    name = if (isJoe) "Joe" else "Jason"
    isReplicant <- b
    model = if (isReplicant) "replicant" else "borg"
  } yield Robot(id, sentient, name, model)

}

createRobot5.runA(Seed(12341234L)).value