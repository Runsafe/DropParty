package no.runsafe.dropparty;

import no.runsafe.framework.api.*;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.wrapper.BukkitWorld;
import no.runsafe.framework.minecraft.Firework;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;
import org.bukkit.Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropHandler implements IConfigurationChanged
{
	public DropHandler(IScheduler scheduler, IDebug output, IServer server)
	{
		this.scheduler = scheduler;
		this.output = output;
		this.server = server;
	}

	public void addItem(RunsafeMeta item)
	{
		this.output.debugFine("%sx %s added to drop loot.", item.getAmount(), item.getNormalName());
		this.items.add(item);
	}

	public void clearItems()
	{
		this.output.debugFine("Drop loot cleared.");
		this.items.clear();
	}

	public boolean dropIsRunning()
	{
		return this.running;
	}

	public void initiateDrop(IPlayer player)
	{
		if (dropLocation == null)
			return;
		server.broadcastMessage(String.format(this.eventMessage, (player == null ? "" : player.getPrettyName())));

		this.droppingItems.addAll(this.items);
		this.items.clear();

		this.scheduler.startSyncTask(new Runnable()
		{
			@Override
			public void run()
			{
				dropNext();
			}
		}, 60);
		this.running = true;
	}

	public void dropNext()
	{
		this.output.debugFine("Item drop iteration...");
		if (!this.droppingItems.isEmpty())
		{
			this.output.debugFine("Items remaining, dropping random one.");
			RunsafeLocation location = null;
			while (location == null)
			{
				RunsafeLocation randomLocation = this.getRandomLocation();
				if (randomLocation.getBlock().isAir())
					location = randomLocation;
			}

			IWorld world = location.getWorld();
			world.dropItem(location, this.droppingItems.get(0));
			((BukkitWorld)world).playEffect(location, Effect.POTION_BREAK, 16417);
			this.droppingItems.remove(0);

			this.scheduler.startSyncTask(new Runnable()
			{
				@Override
				public void run()
				{
					dropNext();
				}
			}, this.spawnTimer);
		}
		else
		{
			this.output.debugFine("Out of items, cancelled");
			this.running = false;
		}
	}

	private RunsafeLocation getRandomLocation()
	{
		if (dropLocation == null)
			return null;
		int highX = this.dropLocation.getBlockX() + this.dropRadius;
		int highZ = this.dropLocation.getBlockZ() + this.dropRadius;
		int lowX = this.dropLocation.getBlockX() - this.dropRadius;
		int lowZ = this.dropLocation.getBlockZ() - this.dropRadius;

		return new RunsafeLocation(
			this.dropLocation.getWorld(),
			this.getRandom(lowX, highX),
			this.dropLocation.getBlockY(),
			this.getRandom(lowZ, highZ)
		);
	}

	private int getRandom(int low, int high)
	{
		return low + (int) (Math.random() * ((high - low) + 1));
	}

	public boolean hasLoot()
	{
		return !this.items.isEmpty();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.eventMessage = configuration.getConfigValueAsString("eventMessage");
		this.dropRadius = configuration.getConfigValueAsInt("dropRadius");
		Map<String, String> configLocation = configuration.getConfigValuesAsMap("dropLocation");
		this.spawnTimer = configuration.getConfigValueAsInt("spawnTimer");

		IWorld world = server.getWorld(configLocation.get("world"));
		this.dropLocation = null;
		if (world != null)
			this.dropLocation = new RunsafeLocation(
				world,
				Integer.valueOf(configLocation.get("x")),
				Integer.valueOf(configLocation.get("y")),
				Integer.valueOf(configLocation.get("z"))
			);
	}

	private final List<RunsafeMeta> items = new ArrayList<RunsafeMeta>();
	private final List<RunsafeMeta> droppingItems = new ArrayList<RunsafeMeta>();
	private RunsafeLocation dropLocation;
	private int dropRadius;
	private final IScheduler scheduler;
	private boolean running = false;
	private final IDebug output;
	private final IServer server;
	private String eventMessage;
	private long spawnTimer;
}
