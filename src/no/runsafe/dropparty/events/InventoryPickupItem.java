package no.runsafe.dropparty.events;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.api.event.inventory.IInventoryPickupItem;
import no.runsafe.framework.minecraft.entity.RunsafeItem;
import no.runsafe.framework.minecraft.event.inventory.RunsafeInventoryPickupItemEvent;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

public class InventoryPickupItem implements IInventoryPickupItem
{
	public InventoryPickupItem(DropHandler dropHandler)
	{
		this.dropHandler = dropHandler;
	}

	@Override
	public void OnInventoryPickupItemEvent(RunsafeInventoryPickupItemEvent event)
	{
		RunsafeInventory inventory = event.getInventory();

		if (inventory.getTitle().equalsIgnoreCase("drop_party_consumer"))
		{
			RunsafeItem item = event.getItem();
			this.dropHandler.addItem(item.getItemStack());
			item.remove();
			event.setCancelled(true);
		}
	}

	private final DropHandler dropHandler;

}
