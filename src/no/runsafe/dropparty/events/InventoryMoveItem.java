package no.runsafe.dropparty.events;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.event.inventory.IInventoryMoveItem;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.block.RunsafeHopper;
import no.runsafe.framework.server.event.inventory.RunsafeInventoryMoveItemEvent;
import no.runsafe.framework.server.inventory.IInventoryHolder;

public class InventoryMoveItem implements IInventoryMoveItem
{
	public InventoryMoveItem(DropHandler handler, IOutput output)
	{
		this.handler = handler;
		this.output = output;
	}

	@Override
	public void OnInventoryMoveItemEvent(RunsafeInventoryMoveItemEvent event)
	{
		this.output.fine("Detected InventoryMoveItemEvent");
		IInventoryHolder destination = event.getDestination().getHolder();
		if (destination instanceof RunsafeHopper)
		{
			this.output.fine("Detected Hopper move event");
			RunsafeHopper hopper = (RunsafeHopper) destination;
			if (hopper.getInventory().getTitle().equalsIgnoreCase("drop_party_consumer"))
			{
				this.output.fine("The name of the hopper matched our drop hoppers.");
				handler.addItem(event.getItem());
				event.setCancelled(true);
			}
		}
	}

	private DropHandler handler;
	private IOutput output;
}
