package inf112.skeleton.app;

import java.util.HashMap;

public class Commands {
    /** Move a robot along its direction vector. */
    public static ICommand moveCommand = (int amount, Robot robot, Game game) -> {
        int sgn = (int) Math.signum(amount);
        Vector2D dir_v = robot.getDir().copy();
        dir_v.mul(sgn);
        for (int i = 0; i < Math.abs(amount); i++) {
            if (!game.canMoveTo(robot.getPos(), dir_v, robot))
                return false;
            robot.move(sgn);
            game.isOnHole(robot);
        }
        return true;
    };

    /** Rotate the direction vector of a robot. */
    public static ICommand rotateCommand = (int amount, Robot robot, Game game) -> {
        robot.rot(amount);
        return true;
    };

    /** Map strings to command functions. */
    private static HashMap<String, ICommand> cmd_map;
    static {
        cmd_map = new HashMap<>();
        cmd_map.put("move", Commands.moveCommand);
        cmd_map.put("rotate", Commands.rotateCommand);
    }

    /**
     * Get a command function by name.
     *
     * @param name Name of the command, see #{cmd_map}
     * @return The command.
     */
    public static ICommand getComand(String name) {
        ICommand cmd = cmd_map.get(name);
        if (cmd == null) {
            throw new IllegalArgumentException("The command '" + name + "' is not defined");
        }
        return cmd;
    }
}