package edu.brown.cs.jfacey.datastructs;

/**
 * This interface defines a kdpoint which is the type that is inserted into
 * kdtrees. It stores values for a 3d binary tree point with getters and
 * setters.
 *
 * @author jfacey
 *
 */
public interface Kdpoint {

  /**
   * Getter for parent value.
   *
   * @return the value of the point's parent
   */
  Kdpoint getParent();

  /**
   * Setter for parent value.
   *
   * @param parent
   *          the parent node of the point
   */
  void setParent(Kdpoint parent);

  /**
   * Getter for left child value.
   *
   * @return the value of the point's left child
   */
  Kdpoint getLeftChild();

  /**
   * Setter for left child value.
   *
   * @param lChild
   *          the left child node of the point's
   */
  void setLeftChild(Kdpoint lChild);

  /**
   * Getter for right child value.
   *
   * @return the value of the point's right child
   */
  Kdpoint getRightChild();

  /**
   * Setter for right child value.
   *
   * @param rChild
   *          the right child node of the point
   */
  void setRightChild(Kdpoint rChild);

  /**
   * Getter for id value.
   *
   * @return the value of the point's id
   */
  int getID();

  /**
   * Setter for id value.
   *
   * @param id
   *          the id of the point
   */
  void setID(int id);

  /**
   * Getter for name value.
   *
   * @return the value of the name of the point
   */
  String getName();

  /**
   * Setter for name value.
   *
   * @param name
   *          the name of the point
   */
  void setName(String name);

  /**
   * Getter for x position value.
   *
   * @return the value of the x coordinate of the point
   */
  double getX();

  /**
   * Setter for x position value.
   *
   * @param posX
   *          the x position of the point
   */
  void setX(double posX);

  /**
   * Getter for y position value.
   *
   * @return the value of the y coordinate of the point
   */
  double getY();

  /**
   * Setter for y position value.
   *
   * @param posY
   *          the y position of the point
   */
  void setY(double posY);

  /**
   * Getter for z position value.
   *
   * @return the value of the z coordinate of the point
   */
  double getZ();

  /**
   * Setter for z position value.
   *
   * @param posZ
   *          the z position of the point
   */
  void setZ(double posZ);
}
