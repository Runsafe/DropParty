package no.runsafe.dropparty.events;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.event.inventory.IInventoryMoveItem;
import no.runsafe.framework.server.block.RunsafeHopper;
import no.runsafe.framework.server.event.inventory.RunsafeInventoryMoveItemEvent;
import no.runsafe.framework.server.inventory.IInventoryHolder;

public class InventoryMoveItem implements IInventoryMoveItem
{
	public InventoryMoveItem(DropHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public void OnInventoryMoveItemEvent(RunsafeInventoryMoveItemEvent event)
	{
		IInventoryHolder destination = event.getDestination().getHolder();
		if (destination instanceof RunsafeHopper)
		{
			RunsafeHopper hopper = (RunsafeHopper) destination;
			if (hopper.getInventory().getTitle().equalsIgnoreCase("drop_party_consumer"))
			{
				handler.addItem(event.getItem());
				event.setCancelled(true);
			}
		}
	}

	private DropHandler handler;
}
