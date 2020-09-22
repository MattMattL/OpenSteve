package com.muss.opensteve.util;

import java.util.ArrayList;
import java.util.List;

public class AIEntityTypes
{
	private List<AIEntityType> types = new ArrayList<AIEntityType>();

	public AIEntityTypes()
	{

	}

	public void add(AIEntityType typeIn)
	{
		if(!this.is(typeIn))
		{
			this.types.add(typeIn);
		}
	}

	public void delete(AIEntityType typeIn)
	{
		this.types.remove(typeIn);
	}

	public boolean is(AIEntityType typeIn)
	{
		if(this.types.contains(typeIn))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
