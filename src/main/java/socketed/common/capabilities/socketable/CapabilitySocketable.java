package socketed.common.capabilities.socketable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import socketed.common.instances.GemCombinationInstance;
import socketed.common.instances.GemInstance;
import socketed.common.config.ForgeConfig;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.socket.GenericSocket;
import socketed.common.socket.TieredSocket;
import socketed.common.util.SocketedUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilitySocketable implements ICapabilitySocketable {
	
	/**
	 * Reference to the ItemStack these sockets belong to
	 */
	private final ItemStack itemStack;
	
	/**
	 * Ordered list of sockets, default size of 0
	 */
	private final List<GenericSocket> sockets = new ArrayList<>();

	/**
	 * List of GemCombinations this item has
	 */
	private final List<GemCombinationInstance> gemCombinations = new ArrayList<>();
	
	/**
	 * Do not use
	 */
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
	@Nonnull
	public List<GenericSocket> getSockets() {
		return this.sockets;
	}
	
	@Override
	@Nullable
	public GenericSocket getSocketAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= this.sockets.size()) return null;
		return this.sockets.get(socketIndex);
	}
	
	@Override
	public void addSocket(GenericSocket socket) {
		if(socket != null && this.sockets.size() < ForgeConfig.COMMON.maxSockets) {
			this.sockets.add(socket);
			this.refreshCombinations();
		}
	}
	
	private void addSocketNoRefresh(GenericSocket socket) {
		if(socket != null && this.sockets.size() < ForgeConfig.COMMON.maxSockets) {
			this.sockets.add(socket);
		}
	}
	
	@Override
	public void addSocketFromNBT(String socketType, NBTTagCompound tags) {
		if(socketType == null || socketType.isEmpty() || tags == null) return;
		
		//TODO: Allow for registering and deserializing custom socket types
		if(socketType.equals(GenericSocket.TYPE_NAME)) {
			this.addSocketNoRefresh(new GenericSocket(tags));
		}
		else if(socketType.equals(TieredSocket.TYPE_NAME)) {
			this.addSocketNoRefresh(new TieredSocket(tags));
		}
		//Don't fresh combinations here to avoid overwriting cached values before they are fully retrieved
	}
	
	@Override
	@Nullable
	public GemInstance replaceSocketAt(GenericSocket newSocket, int socketIndex) {
		if(socketIndex < 0 || socketIndex >= getSocketCount()) return null;
		
		GemInstance gemInOldSocket = this.sockets.get(socketIndex).getGem();
		GemInstance returnGem = null;
		//Check if a gem exists in the socket being replaced
		if(gemInOldSocket != null) {
			//Check if the new socket is filled, otherwise attempt to place the gem into the new socket
			if(!newSocket.isEmpty() || !newSocket.setGem(gemInOldSocket, false)) {
				//If the new socket is filled, or the gem can't be placed into the new socket, return the gem
				returnGem = gemInOldSocket;
			}
		}
		
		//Put new socket in old socket slot, deleting the old socket
		this.sockets.set(socketIndex, newSocket);
		this.refreshCombinations();
		return returnGem;
	}
	
	@Override
	public int getGemCount() {
		int counter = 0;
		for(GenericSocket socket : this.sockets) {
			if(!socket.isEmpty()) counter++;
		}
		return counter;
	}
	
	@Override
	@Nullable
	public GemInstance getGemAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= this.sockets.size()) return null;
		return this.sockets.get(socketIndex).getGem();
	}
	
	@Override
	@Nonnull
	public List<GemInstance> getAllGems(boolean includeDisabled) {
		List<GemInstance> gems = new ArrayList<>();
		for(GenericSocket socket : this.sockets) {
			if(socket.getGem() != null) {
				if(includeDisabled || (!socket.isDisabled() && !socket.isOverridden())) gems.add(socket.getGem());
			}
		}
		return gems;
	}
	
	@Override
	public boolean addGem(GemInstance gem) {
		if(gem == null) return false;
		if(!gem.getGemType().hasEffectsForStack(this.itemStack)) return false;
		for(GenericSocket socket : this.sockets) {
			if(socket.isEmpty() && socket.setGem(gem, false)) {
				this.refreshCombinations();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean replaceGemAt(GemInstance gem, int socketIndex) {
		if(socketIndex < 0 || socketIndex >= this.sockets.size()) return false;
		if(gem == null) return false;
		if(!gem.getGemType().hasEffectsForStack(this.itemStack)) return false;
		if(this.sockets.get(socketIndex).setGem(gem, false)) {
			this.refreshCombinations();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeGemAt(int socketIndex) {
		if(socketIndex < 0 || socketIndex >= this.sockets.size()) return false;
		GenericSocket socket = this.sockets.get(socketIndex);
		if(socket.isEmpty()) return true;
		if(socket.setGem(null, false)) {
			this.refreshCombinations();
			return true;
		}
		return false;
	}
	
	@Override
	@Nonnull
	public List<GemInstance> removeAllGems() {
		List<GemInstance> gems = this.getAllGems(true);
		for(GenericSocket socket : this.sockets) {
			socket.setGem(null, true);
		}
		this.refreshCombinations();
		return gems;
	}
	
	private List<GenericGemEffect> getAllEffectsRaw() {
		List<GenericGemEffect> effects = new ArrayList<>();
		for(GenericSocket socket : this.sockets) {
			effects.addAll(socket.getActiveEffects());
		}
		for(GemCombinationInstance combination : this.gemCombinations) {
			effects.addAll(combination.getGemEffects());
		}
		return effects;
	}
	
	@Override
	@Nonnull
	public List<GenericGemEffect> getAllPossibleEffects() {
		return this.getAllEffectsRaw().stream()
				   .filter(v -> SocketedUtil.isStackValidForSlot(this.itemStack, v.getSlotType()))
				   .collect(Collectors.toList());
	}
	
	@Override
	@Nonnull
	public List<GenericGemEffect> getAllActiveEffects(ISlotType slotType) {
		return this.getAllEffectsRaw().stream()
				   .filter(v -> SocketedUtil.isStackValidForSlot(this.itemStack, v.getSlotType()))
				   .filter(v -> SocketedUtil.doSlotsMatch(slotType, v.getSlotType()))
				   .collect(Collectors.toList());
	}
	
	@Override
	@Nonnull
	public List<GemCombinationInstance> getGemCombinations() {
		return this.gemCombinations;
	}
	
	@Override
	public void addCombinationFromNBT(NBTTagCompound nbt) {
		GemCombinationInstance gemCombination = new GemCombinationInstance(nbt);
		if(gemCombination.validate()) {
			this.gemCombinations.add(gemCombination);
		}
		//Don't fresh combinations here to avoid overwriting cached values before they are fully retrieved
	}
	
	@Override
	public void refreshCombinations() {
		List<String> currentGemTypes = new ArrayList<>();
		int gemCount = 0;
		for(GenericSocket socket : this.sockets) {
			GemInstance gem = socket.getGem();
			if(gem == null || socket.isDisabled()) currentGemTypes.add("");
			else {
				currentGemTypes.add(gem.getGemType().getName());
				gemCount++;
			}
		}
		
		//Reset combinations
		List<GemCombinationInstance> gemCombinationsOld = new ArrayList<>(this.gemCombinations);
		this.gemCombinations.clear();
		for(GenericSocket socket : this.sockets) {
			socket.setOverridden(false);
		}
		
		//Don't bother checking combinations if there are no gems to check
		if(gemCount == 0) return;
		
		List<Integer> socketsToDisable = new ArrayList<>();
		for(GemCombinationType gemCombination : JsonConfig.getSortedGemCombinationData()) {
			//Don't bother continuing to check combinations if theres not enough gems left to make another combination
			if(gemCount <= 1) break;
			
			int matchIndex = gemCombination.matches(currentGemTypes);
			if(matchIndex != -1) {
				GemCombinationInstance instanceNew = new GemCombinationInstance(gemCombination);
				if(!instanceNew.validate()) continue;
				if(!instanceNew.hasGemEffectsForStack(itemStack)) continue;
				
				//Check existing combinations to retain instantiated effect values if the combination was not changed
				boolean preexisting = false;
				for(GemCombinationInstance instanceOld : gemCombinationsOld) {
					if(instanceOld.getGemCombinationType() == instanceNew.getGemCombinationType()) {
						this.gemCombinations.add(instanceOld);
						preexisting = true;
						break;
					}
				}
				if(!preexisting) this.gemCombinations.add(instanceNew);
				
				//Remove gems that belong to a combination already and optionally disable sockets belonging to them
				if(gemCombination.getIsStrictOrder()) {
					//For strict order, match index is at the start of the list, so just remove in order
					for(int i = 0; i < gemCombination.getGemTypes().size(); i++) {
						//Wrap index, wont ever wrap anyways if wrap isnt enabled
						int socketIndex = (i + matchIndex) % currentGemTypes.size();
						//Disable socket if effect matches
						if(gemCombination.getReplacesEffects()) socketsToDisable.add(socketIndex);
						//Remove gem for following combination checks
						//TODO: Maybe config option to allow for overlapping combinations?
						currentGemTypes.set(socketIndex, "");
						gemCount--;
					}
				}
				else {
					//Non strict order, just remove first instance found of each matched type
					for(String gemToRemove : gemCombination.getGemTypes()) {
						int socketIndex = currentGemTypes.indexOf(gemToRemove);
						//Disable socket if effect matches
						if(gemCombination.getReplacesEffects()) socketsToDisable.add(socketIndex);
						//Remove gem for following combination checks
						//TODO: Maybe config option to allow for overlapping combinations?
						currentGemTypes.set(socketIndex, "");
						gemCount--;
					}
				}
			}
		}
		//Disable sockets overwritten by combinations
		for(int socketIndex : socketsToDisable) {
			GenericSocket socket = this.getSocketAt(socketIndex);
			if(socket != null) socket.setOverridden(true);
		}
	}
}