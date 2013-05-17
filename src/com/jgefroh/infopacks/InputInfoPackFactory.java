package com.jgefroh.infopacks;

import com.jgefroh.components.InputComponent;
import com.jgefroh.core.IEntity;


public class InputInfoPackFactory implements IInfoPackFactory
{
	@Override
	public IInfoPack generate(final IEntity entity)
	{
		if(entity.getComponent(InputComponent.class)!=null)
		{
			return new InputInfoPack(entity);
		}
		return null;
	}

}