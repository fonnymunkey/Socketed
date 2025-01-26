package socketed.common.capabilities;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.socket.GenericSocket;
import socketed.common.socket.TieredSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilitySocketable implements ICapabilitySocketable {
	
	/**
	 * Copy of the itemstack these sockets belong to
	 */
	private final ItemStack itemStack;
	
	/**
	 * Ordered list of sockets, default size of 0 sockets
	 */
	private final List<GenericSocket> sockets = new ArrayList<>();
	
	/**
	 * List of GemCombinations this item has
	 */
	private final List<GemCombinationInstance> gemCombinations = new ArrayList<>();
	
	public CapabilitySocketable() {
		this(null);
	}
	
	public CapabilitySocketable(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
	public int getSocketCount() {
		return this.sockets.size();
	}
	
	@Override
	@Nullable
	public GenericSocket getSocketAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
		return sockets.get(socketIndex);
	}
	
	@Override
	@Nonnull
	@Deprecated
	public List<GemInstance> setSocketCount(int newSocketCount) {
		//If setSocketCount increases socketCount, add empty sockets in socketedGems at end of list
		while(newSocketCount > this.sockets.size()) {
			this.sockets.add(new GenericSocket());
		}
		//Remove sockets if setSocketCount decreases socketCount, return List of gems that have been removed
		List<GemInstance> removedGems = new ArrayList<>();
		while(newSocketCount < this.sockets.size()) {
			int lastIndex = this.sockets.size() - 1;
			if(!this.sockets.get(lastIndex).isEmpty()) {
				removedGems.add(this.sockets.get(lastIndex).getGem());
				sockets.remove(lastIndex);
			}
		}
		return removedGems;
	}
	
	@Override
	public void addSocket(GenericSocket socket) {
		if(socket != null) this.sockets.add(socket);
	}
	
	@Override
	public boolean addSocketFromNBT(String socketType, NBTTagCompound tags) {
		if(socketType.equals("Generic")) {
			addSocket(new GenericSocket(tags));
			return true;
		}
		if(socketType.equals("Tiered")) {
			addSocket(new TieredSocket(tags));
			return true;
		}
		return false;
	}
	
	@Override
	@Nullable
	public GemInstance replaceSocketAt(GenericSocket newSocket, int socketIndex) {
		if(socketIndex < 0 || socketIndex >= getSocketCount()) return null;
		
		GemInstance gemInOldSocket = sockets.get(socketIndex).getGem();
		GemInstance returnGem = null;
		//Check if a gem exists in the socket being replaced
		if(gemInOldSocket != null) {
			//Check if the new socket is filled, otherwise attempt to place the gem into the new socket
			if(!newSocket.isEmpty() || !newSocket.setGem(gemInOldSocket)) {
				//If the new socket is filled, or the gem can't be placed into the new socket, return the gem
				returnGem = gemInOldSocket;
			}
		}
		
		//Put new socket in old socket slot, deleting the old socket
		this.sockets.set(socketIndex, newSocket);
		refreshCombinations();
		return returnGem;
	}
	
	@Override
	public int getGemCount() {
		int counter = 0;
		for(GenericSocket socket : sockets) {
			if(!socket.isEmpty()) counter++;
		}
		return counter;
	}
	
	@Override
	@Nullable
	public GemInstance getGemAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
		return this.sockets.get(socketIndex).getGem();
	}
	
	@Override
	@Nonnull
	public List<GemInstance> getAllGems(boolean includeDisabled) {
		List<GemInstance> gems = new ArrayList<>();
		for(GenericSocket socket : this.sockets) {
			if(socket.getGem() != null && (includeDisabled || !socket.isDisabled())) gems.add(socket.getGem());
		}
		return gems;
	}
	
	@Override
	public boolean addGem(@Nonnull GemInstance gem) {
		if(!gem.hasEffectsForStackDefaultSlot(this.itemStack)) return false;
		for(GenericSocket socket : sockets) {
			if(socket.isEmpty() && socket.setGem(gem)) {
				refreshCombinations();
				return true;
			}
		}
		return false;
	}
	
	@Override
	@Nullable
	public GemInstance replaceGemAt(@Nonnull GemInstance gem, int socketIndex) {
		if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
		if(!gem.hasEffectsForStackDefaultSlot(this.itemStack)) return null;
		GemInstance oldGem = sockets.get(socketIndex).getGem();
		sockets.get(socketIndex).setGem(gem);
		refreshCombinations();
		return oldGem;
	}
	
	@Override
	@Nullable
	public GemInstance removeGemAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= sockets.size()) return null;
		GenericSocket socket = sockets.get(socketIndex);
		if(socket.isEmpty()) return null;
		GemInstance returnGem = socket.getGem();
		socket.setGem(null);
		refreshCombinations();
		return returnGem;
	}
	
	@Override
	@Nonnull
	public List<GemInstance> removeAllGems() {
		List<GemInstance> gems = getAllGems(true);
		for(GenericSocket socket : sockets){
			socket.setGem(null);
		}
		gemCombinations.clear();
		return gems;
	}
	
	@Override
	@Nonnull
	public List<GenericGemEffect> getAllEffects() {
		List<GenericGemEffect> effects = new ArrayList<>();
		for(GenericSocket socket : sockets) {
			if(!socket.isDisabled()) effects.addAll(socket.getEffects());
		}
		for(GemCombinationInstance combination : this.gemCombinations) {
			effects.addAll(combination.getGemEffects());
		}
		return effects;
	}
	
	@Override
	@Nonnull
	public List<GenericGemEffect> getAllEffectsForSlot(EntityEquipmentSlot slot) {
		return getAllEffects().stream()
							  .filter(v -> v.getSlots().contains(slot))
							  .collect(Collectors.toList());
	}
	
	@Nonnull
	@Override
	public List<GemCombinationInstance> getGemCombinations() {
		return this.gemCombinations;
	}
	
	@Override
	public void addCombinationFromNBT(NBTTagCompound nbt) {
		GemCombinationInstance gemCombination = new GemCombinationInstance(nbt);
		if(gemCombination.getGemCombinationType() != null) {
			this.gemCombinations.add(gemCombination);
		}
	}
	
	@Override
	public void refreshCombinations() {
		List<String> currentGemTypes = new ArrayList<>();
		for(GenericSocket socket : this.sockets) {
			GemInstance gem = socket.getGem();
			if(gem == null) {
				currentGemTypes.add("");
				continue;
			}
			GemType gemType = socket.getGem().getGemType();
			currentGemTypes.add(gemType == null ? "" : gemType.getName());
		}
		
		//Reset
		this.gemCombinations.clear();
		for(GenericSocket socket : this.sockets) {
			socket.setDisabled(false);
		}
		
		List<Integer> socketsToDisable = new ArrayList<>();
		for(GemCombinationType gemCombination : JsonConfig.getSortedGemCombinationData()) {
			if(gemCombination.matches(currentGemTypes)) {
				GemCombinationInstance inst = new GemCombinationInstance(gemCombination);
				if(!inst.hasEffectsForStackDefaultSlot(itemStack)) continue;
				this.gemCombinations.add(inst);
				//Remove gems that belong to a combination already and optionally disable sockets belonging to them
				
				List<String> gemsInCombination = gemCombination.getGemTypes();
				for(String gemToRemove : gemsInCombination) {
					int socketIndex = currentGemTypes.indexOf(gemToRemove);
					if(socketIndex != -1) {
						if(gemCombination.getReplacesEffects()) socketsToDisable.add(socketIndex);
						//Gem already found, don't find it again (no remove to keep indexing same as sockets)
						currentGemTypes.set(socketIndex, "");
					}
				}
			}
		}
		//Enable/Disable sockets
		for(int socketIndex : socketsToDisable) {
			GenericSocket socket = this.getSocketAt(socketIndex);
			if(socket != null) socket.setDisabled(true);
		}
	}
}