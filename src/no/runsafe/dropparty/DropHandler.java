package no.runsafe.dropparty;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.server.RunsafeLocation;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.RunsafeWorld;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;
import org.bukkit.Effect;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropHandler implements IConfigurationChanged
{
	public DropHandler(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	public void addItem(RunsafeItemStack item)
	{
		this.items.add(item);
	}

	public void clearItems()
	{
		this.items.clear();
	}

	public boolean dropIsRunning()
	{
		return this.running;
	}

	public void initiateDrop(RunsafePlayer player)
	{
		RunsafeServer.Instance.broadcastMessage(
			String.format(
				"%s &3has initiated a drop-party in the party room, starting in one minute! Use /dropparty to get there!",
				(player == null ? "" : player.getPrettyName())
			)
		);

		this.scheduler.startSyncTask(new Runnable() {
			@Override
			public void run() {
				dropNext();
			}
		}, 60);
		this.running = true;
	}

	public void dropNext()
	{
		if (!this.items.isEmpty())
		{
			RunsafeLocation location = null;
			while (location == null)
			{
				RunsafeLocation randomLocation = this.getRandomLocation();
				if (randomLocation.getBlock().getTypeId() == Material.AIR.getId())
					location = randomLocation;
			}

			RunsafeWorld world = location.getWorld();
			world.dropItem(location, this.items.get(0));
			world.playEffect(location, Effect.POTION_BREAK, 16417);
			this.items.remove(0);

			this.scheduler.startSyncTask(new Runnable() {
				@Override
				public void run() {
					dropNext();
				}
			}, 3);
		}
		else
		{
			this.running = false;
		}
	}

	private RunsafeLocation getRandomLocation()
	{
		int highX = this.dropLocation.getBlockX() + this.dropRadius;
		int highZ = this.dropLocation.getBlockZ() + this.dropRadius;
		int lowX = this.dropLocation.getBlockX() + this.dropRadius;
		int lowZ = this.dropLocation.getBlockZ() + this.dropRadius;

		return new RunsafeLocation(
				this.dropLocation.getWorld(),
				this.getRandom(lowX, highX),
				this.dropLocation.getBlockY(),
				this.getRandom(lowZ, highZ)
		);
	}

	private int getRandom(int low, int high)
	{
		return low + (int)(Math.random() * ((high - low) + 1));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.dropRadius = configuration.getConfigValueAsInt("dropRadius");
		Map<String, String> configLocation = configuration.getConfigValuesAsMap("dropLocation");

		this.dropLocation = new RunsafeLocation(
				RunsafeServer.Instance.getWorld(configLocation.get("world")),
				Integer.valueOf(configLocation.get("x")),
				Integer.valueOf(configLocation.get("y")),
				Integer.valueOf(configLocation.get("z"))
		);
	}

	private List<RunsafeItemStack> items = new ArrayList<RunsafeItemStack>();
	private RunsafeLocation dropLocation;
	private int dropRadius;
	private IScheduler scheduler;
	private boolean running = false;
}