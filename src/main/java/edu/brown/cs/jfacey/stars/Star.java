package edu.brown.cs.jfacey.stars;

public class Star implements Kdpoint {
	
	private Kdpoint _parent;
	private Kdpoint _lChild;
	private Kdpoint _rChild;
	private int _starID;
	private String _name;
	private double _posX;
	private double _posY;
	private double _posZ;
	
	public Star(int starID, String name, double positionX, double positionY, double positionZ) {
		_starID = starID;
		_name = name;
		_posX = positionX;
		_posY = positionY;
		_posZ = positionZ;
		
	}
	@Override
	public Kdpoint getParent() {
		return _parent;
	}

	@Override
	public void setParent(Kdpoint parent) {
		_parent = parent;
	}

	@Override
	public Kdpoint getLeftChild() {
		return _lChild;
	}

	@Override
	public void setLeftChild(Kdpoint lChild) {
		_lChild = lChild;
		
	}

	@Override
	public Kdpoint getRightChild() {
		return _rChild;
	}

	@Override
	public void setRightChild(Kdpoint rChild) {
		_rChild = rChild;
		
	}
	@Override
	public int getID() {
		return _starID;
	}
	@Override
	public void setID(int id) {
		_starID = id;
	}
	@Override
	public String getName() {
		return _name;
	}
	@Override
	public void setName(String name) {
		_name = name;
	}
	@Override
	public double getX() {
		return _posX;
	}
	@Override
	public void setX(double posX) {
		_posX = posX;
	}
	@Override
	public double getY() {
		return _posY;
	}
	@Override
	public void setY(double posY) {
		_posY = posY;
	}
	@Override
	public double getZ() {
		return _posZ;
	}
	@Override
	public void setZ(double posZ) {
		_posZ = posZ;
	}

	
}
