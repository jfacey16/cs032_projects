package edu.brown.cs.jfacey.stars;

public interface Kdpoint {
	
	public Kdpoint getParent();
	public void setParent(Kdpoint parent);
	public Kdpoint getLeftChild();
	public void setLeftChild(Kdpoint lChild);
	public Kdpoint getRightChild();
	public void setRightChild(Kdpoint rChild);
	public int getID();
	public void setID(int id);
	public String getName();
	public void setName(String name);
	public double getX();
	public void setX(double posX);
	public double getY();
	public void setY(double posY);
	public double getZ();
	public void setZ(double posZ);
}
