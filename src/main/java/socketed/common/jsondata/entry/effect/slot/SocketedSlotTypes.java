package socketed.common.jsondata.entry.effect.slot;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public enum SocketedSlotTypes implements ISlotType {
	
	@SerializedName("ALL")
	ALL {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return true;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return true;
		}
		
		@Override
		public String getTooltipKey() {
			return "all";
		}
	},
	@SerializedName("NONE")
	NONE {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return false;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return false;
		}
		
		@Override
		public String getTooltipKey() {
			return "none";
		}
	},
	
	@SerializedName("BODY")
	BODY {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(stack);
			return slot == EntityEquipmentSlot.HEAD || slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.HEAD || slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
		}
		
		@Override
		public String getTooltipKey() {
			return "body";
		}
	},
	@SerializedName("HEAD")
	HEAD {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.HEAD;
		}
		
		@Override
		public String getTooltipKey() {
			return "head";
		}
	},
	@SerializedName("CHEST")
	CHEST {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.CHEST;
		}
		
		@Override
		public String getTooltipKey() {
			return "chest";
		}
	},
	@SerializedName("LEGS")
	LEGS {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.LEGS;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.LEGS;
		}
		
		@Override
		public String getTooltipKey() {
			return "legs";
		}
	},
	@SerializedName("FEET")
	FEET {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.FEET;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.FEET;
		}
		
		@Override
		public String getTooltipKey() {
			return "feet";
		}
	},
	
	@SerializedName("HAND")
	HAND {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(stack);
			return slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND;
		}
		
		@Override
		public String getTooltipKey() {
			return "hand";
		}
	},
	@SerializedName("MAINHAND")
	MAINHAND {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.MAINHAND;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.MAINHAND;
		}
		
		@Override
		public String getTooltipKey() {
			return "mainhand";
		}
	},
	@SerializedName("OFFHAND")
	OFFHAND {
		
		@Override
		public boolean isStackValid(ItemStack stack) {
			return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.OFFHAND;
		}
		
		@Override
		public boolean isSlotValid(EntityEquipmentSlot slot) {
			return slot == EntityEquipmentSlot.OFFHAND;
		}
		
		@Override
		public String getTooltipKey() {
			return "offhand";
		}
	}
}