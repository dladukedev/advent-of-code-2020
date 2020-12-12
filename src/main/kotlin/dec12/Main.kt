package dec12

import java.lang.Exception
import kotlin.math.abs

enum class Direction {
    North,
    South,
    East,
    West
}

data class State(
    val northUnits: Int = 0,
    val southUnits: Int = 0,
    val eastUnits: Int = 0,
    val westUnits: Int = 0,
    val facing: Direction = Direction.East
)

sealed class Action {
    data class Forward(val units: Int) : Action()

    data class TurnRight(val degrees: Int) : Action()
    data class TurnLeft(val degrees: Int) : Action()

    data class GoNorth(val units: Int) : Action()
    data class GoSouth(val units: Int) : Action()
    data class GoEast(val units: Int) : Action()
    data class GoWest(val units: Int) : Action()
}

fun reducer(action: Action, state: State = State()): State {
    return when (action) {
        is Action.Forward -> {
            when (state.facing) {
                Direction.North -> state.copy(northUnits = state.northUnits + action.units)
                Direction.South -> state.copy(southUnits = state.southUnits + action.units)
                Direction.East -> state.copy(eastUnits = state.eastUnits + action.units)
                Direction.West -> state.copy(westUnits = state.westUnits + action.units)
            }
        }

        is Action.TurnRight -> {
            val directionChanges = action.degrees / 90
            val directions = listOf(Direction.North, Direction.East, Direction.South, Direction.West)
            val facingIndex = directions.indexOf(state.facing)
            val newFacingIndex = (directionChanges + facingIndex) % 4
            val newFacing = directions[newFacingIndex]

            state.copy(facing = newFacing)
        }
        is Action.TurnLeft -> {
            val directionChanges = action.degrees / 90
            val directions = listOf(Direction.North, Direction.West, Direction.South, Direction.East)
            val facingIndex = directions.indexOf(state.facing)
            val newFacingIndex = (directionChanges + facingIndex) % 4
            val newFacing = directions[newFacingIndex]

            state.copy(facing = newFacing)
        }

        is Action.GoNorth -> state.copy(northUnits = state.northUnits + action.units)
        is Action.GoSouth -> state.copy(southUnits = state.southUnits + action.units)
        is Action.GoEast -> state.copy(eastUnits = state.eastUnits + action.units)
        is Action.GoWest -> state.copy(westUnits = state.westUnits + action.units)
    }
}

data class Waypoint(
    val verticalUnits: Int = 1,
    val horizontalUnits: Int = 10
)

data class WaypointState(
    val horizontalUnits: Int = 0,
    val verticalUnits: Int = 0,
    val waypoint: Waypoint = Waypoint()
)


fun calculateTurn(action: Action, waypoint: Waypoint): Waypoint {
    return when(action) {
        is Action.TurnRight -> {
            val turns = action.degrees / 90
            when(turns % 4) {
                0 -> {
                    waypoint
                }
                1 -> {
                    waypoint.copy(verticalUnits = -waypoint.horizontalUnits, horizontalUnits = waypoint.verticalUnits)
                }
                2 -> {
                    waypoint.copy(verticalUnits = -waypoint.verticalUnits, horizontalUnits = -waypoint.horizontalUnits)
                }
                3 -> {
                    waypoint.copy(verticalUnits = waypoint.horizontalUnits, horizontalUnits = -waypoint.verticalUnits)
                }
                else -> throw Exception("Invalid turn count: $turns")
            }
        }
        is Action.TurnLeft -> {
            val turns = action.degrees / 90
            when(turns % 4) {
                0 -> {
                    waypoint
                }
                1 -> {
                    waypoint.copy(verticalUnits = waypoint.horizontalUnits, horizontalUnits = -waypoint.verticalUnits)
                }
                2 -> {
                    waypoint.copy(verticalUnits = -waypoint.verticalUnits, horizontalUnits = -waypoint.horizontalUnits)
                }
                3 -> {
                    waypoint.copy(verticalUnits = -waypoint.horizontalUnits, horizontalUnits = waypoint.verticalUnits)
                }
                else -> throw Exception("Invalid turn count: $turns")
            }
        }
        else -> waypoint
    }
}


fun waypointReducer(action: Action, state: WaypointState = WaypointState()): WaypointState {
    return when (action) {
        is Action.Forward -> {
            val verticalProgress = state.waypoint.verticalUnits * action.units
            val horizontalProgress = state.waypoint.horizontalUnits * action.units

            state.copy(
                verticalUnits = state.verticalUnits + verticalProgress,
                horizontalUnits = state.horizontalUnits + horizontalProgress
            )
        }

        is Action.TurnRight, is Action.TurnLeft -> {
            val newWaypoint = calculateTurn(action, state.waypoint)
            state.copy(waypoint = newWaypoint)
        }

        is Action.GoNorth -> {
            val newWaypoint = state.waypoint.copy(verticalUnits = state.waypoint.verticalUnits + action.units)
            state.copy(waypoint = newWaypoint)
        }
        is Action.GoSouth -> {
            val newWaypoint = state.waypoint.copy(verticalUnits = state.waypoint.verticalUnits - action.units)
            state.copy(waypoint = newWaypoint)
        }
        is Action.GoEast -> {
            val newWaypoint = state.waypoint.copy(horizontalUnits = state.waypoint.horizontalUnits + action.units)
            state.copy(waypoint = newWaypoint)
        }
        is Action.GoWest -> {
            val newWaypoint = state.waypoint.copy(horizontalUnits = state.waypoint.horizontalUnits - action.units)
            state.copy(waypoint = newWaypoint)
        }
    }
}

fun calculateManhattanDistance(state: State): Int {
    val northSouth = abs(state.northUnits - state.southUnits)
    val eastWest = abs(state.eastUnits - state.westUnits)

    return northSouth + eastWest
}

fun parseInput(input: String): List<Action> {
    return input.lines()
        .map {
            val actionChar = it[0]
            val amount = it.substring(1).toInt()

            when (actionChar) {
                'N' -> Action.GoNorth(amount)
                'S' -> Action.GoSouth(amount)
                'E' -> Action.GoEast(amount)
                'W' -> Action.GoWest(amount)

                'L' -> Action.TurnLeft(amount)
                'R' -> Action.TurnRight(amount)

                'F' -> Action.Forward(amount)
                else -> throw Exception("Unknown character: $actionChar")
            }
        }
}


fun main() {
    val actions = parseInput(input)

    println("------------ PART 1 ------------")
    val endState = actions.fold(State()) { acc, action ->
        reducer(action, acc)
    }
    val result = calculateManhattanDistance(endState)
    println("result: $result")

    println("------------ PART 2 ------------")
    val endWaypointState = actions.fold(WaypointState()) { acc, action ->
        waypointReducer(action, acc)
    }
    val result2 = abs(endWaypointState.horizontalUnits) + abs(endWaypointState.verticalUnits)
    println("result: $result2")
}