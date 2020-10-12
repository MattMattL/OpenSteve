package com.muss.opensteve.entity.ai.brain;

import com.muss.opensteve.util.OpenSteveStatics;

import java.util.ArrayList;
import java.util.List;

public class BackPropHelper
{
	private List<Factor> factorList = new ArrayList<>();

	@Deprecated
	public void create(String key, int length, Type type)
	{
		Factor factor = new Factor(key, length);

		this.factorList.add(factor);
	}

	public void create(String key, int length)
	{
		Factor factor = new Factor(key, length);

		this.factorList.add(factor);
	}

	public Factor getKey(String key)
	{
		for(Factor factor : this.factorList)
		{
			if(key.equals(factor.key))
				return factor;
		}

		OpenSteveStatics.print("BackPropHelper::get", "error: no factor with matching key");
		return null;
	}

	public void tick()
	{
		for(Factor factor : this.factorList)
		{
			factor.tick();
		}
	}

	@Deprecated
	public static enum Type
	{
		FLOAT,
		INT,
		DOUBLE,
		BOOLEAN;
	}

	public class Data
	{
		private double value;

		public void setValue(double valueIn)
		{
			this.value = valueIn;
		}

		public void setValue(float valueIn)
		{
			this.value = (double)valueIn;
		}

		public void setValue(int valueIn)
		{
			this.value = (double)valueIn;
		}

		public double value()
		{
			return this.value;
		}
	}

	public class Factor
	{
		public final String key;
		public final int length;

		private List<Data> data = new ArrayList<>();
		private int offset;

		public Factor(String keyIn, int lengthIn)
		{
			this.key = keyIn;
			this.length = lengthIn;

			this.offset = 0;

			for(int i=0; i<lengthIn; i++)
				this.data.add(new Data());
		}

		public void tick()
		{
			this.offset = (++this.offset < this.length) ? this.offset : this.offset % this.length;
		}

		public Data at()
		{
			return this.data.get(offset);
		}

		public Data at(int index)
		{
			index += this.offset;
			index = (index < 0) ? index % this.length + this.length : index % this.length;

			return this.data.get(index);
		}
	}
}
