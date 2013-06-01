package com.jgefroh.systems;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.jgefroh.core.Core;
import com.jgefroh.core.IEntity;
import com.jgefroh.core.ISystem;
import com.jgefroh.core.LoggerFactory;
import com.jgefroh.infopacks.HealthInfoPack;


/**
 * This system is in charge of dealing damage.
 * @author Joseph Gefroh
 */
public class DamageSystem implements ISystem
{
	//////////
	// DATA
	//////////
	/**A reference to the core engine controlling this system.*/
	private Core core;
	
	/**Flag that shows whether the system is running or not.*/
	@SuppressWarnings("unused")
	private boolean isRunning;
	
	/**The time to wait between executions of the system.*/
	private long waitTime;
	
	/**The time this System was last executed, in ms.*/
	private long last;
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.FINE;
	
	/**Logger for debug purposes.*/
	private final Logger LOGGER 
		= LoggerFactory.getLogger(this.getClass(), debugLevel);
	
	
	//////////
	// INIT
	//////////
	/**
	 * Create a new instance of this {@code System}.
	 * @param core	 a reference to the Core controlling this system
	 */
	public DamageSystem(final Core core)
	{
		this.core = core;
		init();
	}

	/////////
	// ISYSTEM INTERFACE
	/////////
	@Override
	public void init()
	{
		isRunning = true;		
		core.setInterested(this, "FORT_HIT");
		core.setInterested(this, "PLAYER_HIT");
		core.setInterested(this, "ALIEN_HIT");
	}
	
	@Override
	public void start()
	{
		LOGGER.log(Level.INFO, "System started.");
		isRunning = true;
	}

	@Override
	public void work(final long now)
	{
	}

	@Override
	public void stop()
	{
		LOGGER.log(Level.INFO, "System stopped.");
		isRunning = false;
	}
	
	@Override
	public long getWait()
	{
		return this.waitTime;
	}

	@Override
	public long	getLast()
	{
		return this.last;
	}
	
	@Override
	public void setWait(final long waitTime)
	{
		this.waitTime = waitTime;
	}
	
	@Override
	public void setLast(final long last)
	{
		this.last = last;
	}
	
	@Override
	public void recv(final String id, final String... message)
	{
		if(id.equals("FORT_HIT"))
		{
			HealthInfoPack pack = core.getInfoPackFrom(message[0], HealthInfoPack.class);
			damage(1,pack);
		}
		else if(id.equals("PLAYER_HIT"))
		{
			HealthInfoPack pack = core.getInfoPackFrom(message[0], HealthInfoPack.class);
			damage(20,pack);
			core.send("HEALTH_UPDATE", pack.getCurHealth() + "");
		}
	}
	/////////
	// SYSTEM METHODS
	/////////
	/**
	 * Deal damage to an entity.
	 * @param amount	the amount of damage to deal
	 * @param source	the source of the damage
	 * @param target	the receiver of the damage
	 */
	public void damage(final int amount, final IEntity source, final IEntity target)
	{
		//TODO: Remove amount, deal based on weapon damage? Too coupled?
		HealthInfoPack hip = core.getInfoPackFrom(target, HealthInfoPack.class);
		if(hip!=null)
		{
			hip.setCurHealth(hip.getCurHealth()-amount);
		}
	}
	
	/**
	 * Deal damage to an Entity.
	 * @param amount	the amount of damage to deal
	 * @param pack		the HealthInfopack of the entity to damage
	 */
	public void damage(final int amount, final HealthInfoPack pack)
	{
		if(pack!=null)
		{
			pack.setCurHealth(pack.getCurHealth()-amount);
		}
	}
}
