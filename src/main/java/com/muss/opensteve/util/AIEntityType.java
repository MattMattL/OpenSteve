package com.muss.opensteve.util;

public enum AIEntityType
{
	ALEX("Alex", 0b00000001),
	STEVE("Steve", 0b00000001),

	ADULT("Adult", 0b00000010),
	CHILD("Child", 0b00000010);

	private final String name;
	private final int mutexBit;

	private AIEntityType(String nameIn, int mutexBitIn)
	{
		this.name = nameIn;
		this.mutexBit = mutexBitIn;
	}

	public String getString()
	{
		return this.name;
	}

	public int getMutexBit()
	{
		return this.mutexBit;
	}

	public boolean compatibleWith(AIEntityType type)
	{
		int bit = this.mutexBit & type.getMutexBit();

		return (bit == 0) ? true : false;
	}
}
