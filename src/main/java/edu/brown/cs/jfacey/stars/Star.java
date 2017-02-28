package edu.brown.cs.jfacey.stars;

import edu.brown.cs.jfacey.datastructs.Kdpoint;

/**
 * This class is an implementation of the kdpoint class that is specific to the
 * stars project. It just defines all of the getter and setter methods.
 *
 * @author jfacey
 *
 */
public class Star implements Kdpoint {

  private Kdpoint iParent;
  private Kdpoint iLChild;
  private Kdpoint iRChild;
  private int iStarID;
  private String iName;
  private double iPosX;
  private double iPosY;
  private double iPosZ;

  /**
   * This is the basic constructor for a star, which as implementation of
   * kdpoint. It just sets the values associated with the star to the passed in
   * parameters.
   *
   * @param starID
   *          the id number of the star
   * @param name
   *          the name of the star
   * @param positionX
   *          the x coordinate of the star
   * @param positionY
   *          the y coordinate of the star
   * @param positionZ
   *          the z coordinate of the star
   */
  public Star(int starID, String name, double positionX, double positionY,
      double positionZ) {
    iStarID = starID;
    iName = name;
    iPosX = positionX;
    iPosY = positionY;
    iPosZ = positionZ;

  }

  /**
   * Getter for parent value.
   *
   * @return the value of the stars' parent
   */
  @Override
  public Kdpoint getParent() {
    return iParent;
  }

  /**
   * Setter for parent value.
   *
   * @param parent
   *          the parent node of the star
   */
  @Override
  public void setParent(Kdpoint parent) {
    iParent = parent;
  }

  /**
   * Getter for left child value.
   *
   * @return the value of the stars' left child
   */
  @Override
  public Kdpoint getLeftChild() {
    return iLChild;
  }

  /**
   * Setter for left child value.
   *
   * @param lChild
   *          the left child node of the star
   */
  @Override
  public void setLeftChild(Kdpoint lChild) {
    iLChild = lChild;
  }

  /**
   * Getter for right child value.
   *
   * @return the value of the stars' right child
   */
  @Override
  public Kdpoint getRightChild() {
    return iRChild;
  }

  /**
   * Setter for right child value.
   *
   * @param rChild
   *          the right child node of the star
   */
  @Override
  public void setRightChild(Kdpoint rChild) {
    iRChild = rChild;
  }

  /**
   * Getter for id value.
   *
   * @return the value of the stars' id
   */
  @Override
  public int getID() {
    return iStarID;
  }

  /**
   * Setter for id value.
   *
   * @param id
   *          the id of the star
   */
  @Override
  public void setID(int id) {
    iStarID = id;
  }

  /**
   * Getter for name value.
   *
   * @return the value of the name of the star
   */
  @Override
  public String getName() {
    return iName;
  }

  /**
   * Setter for name value.
   *
   * @param name
   *          the name of the star
   */
  @Override
  public void setName(String name) {
    iName = name;
  }

  /**
   * Getter for x position value.
   *
   * @return the value of the x coordinate of the star
   */
  @Override
  public double getX() {
    return iPosX;
  }

  /**
   * Setter for x position value.
   *
   * @param posX
   *          the x position of the star
   */
  @Override
  public void setX(double posX) {
    iPosX = posX;
  }

  /**
   * Getter for y position value.
   *
   * @return the value of the y coordinate of the star
   */
  @Override
  public double getY() {
    return iPosY;
  }

  /**
   * Setter for y position value.
   *
   * @param posY
   *          the y position of the star
   */
  @Override
  public void setY(double posY) {
    iPosY = posY;
  }

  /**
   * Getter for z position value.
   *
   * @return the value of the z coordinate of the star
   */
  @Override
  public double getZ() {
    return iPosZ;
  }

  /**
   * Setter for z position value.
   *
   * @param posZ
   *          the z position of the star
   */
  @Override
  public void setZ(double posZ) {
    iPosZ = posZ;
  }
}
