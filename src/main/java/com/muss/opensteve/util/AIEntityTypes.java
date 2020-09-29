package com.muss.opensteve.util;

import java.util.ArrayList;
import java.util.List;

public class AIEntityTypes
{
	private List<AIEntityType> entityTypeList = new ArrayList<AIEntityType>();

	public AIEntityTypes()
	{

	}

	public void add(AIEntityType type)
	{
		if(!this.has(type))
		{
			this.entityTypeList.add(type);
		}

		this.removeIncompatibles(type);
	}

	private void removeIncompatibles(AIEntityType type)
	{
		int i = 0;

		while(i < this.entityTypeList.size())
		{
			AIEntityType element = this.entityTypeList.get(i);

			if(!element.compatibleWith(type) && element != type)
			{
				this.entityTypeList.remove(i);
			}
			else
			{
				i++;
			}
		}
	}

	public void delete(AIEntityType type)
	{
		this.entityTypeList.remove(type);
	}

	public boolean has(AIEntityType type)
	{
		return (this.entityTypeList.contains(type)) ? true : false;
	}

	/* DEBUG */
	public void printAllTypes()
	{
		this.printAllTypes("");
	}

	/* DEBUG */
	public void printAllTypes(String message)
	{
		System.out.printf("[OpenSteve] %s\n", message);

		for(AIEntityType type : this.entityTypeList)
		{
			System.out.printf("  %s\n", type.getString());
		}

		System.out.printf("\n");
	}
}
