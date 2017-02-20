package edu.brown.cs.jfacey.readers;

/**
 * This interface defines a command, which is taken in by a repl and can be
 * processed based on the input read by the repl.
 *
 * @author jfacey
 *
 */
public interface Command {

  /**
   * This method defines the commands actual logic. In it, the command will
   * check if the input is correct and then attempt to execute the input. The
   * method will return true if this is the command that the user is trying to
   * execute, whether it was formatted correctly or ran correctly. If a
   * different command was input, it will return false.
   *
   * @return a boolean based on if this is the command or not
   *
   */
  boolean execute(String[] inputs);
}
