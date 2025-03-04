package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;
import socketed.common.socket.gem.effect.activatable.callback.GenericEventCallback;
import socketed.common.socket.gem.effect.activatable.callback.IEffectCallback;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class DamageSourceCondition extends GenericCondition {
	public static final String TYPE_NAME = "Damage Source";

	@SerializedName("Allows Melee")
	protected Boolean allowsMelee;
	@SerializedName("Allows Ranged")
	protected Boolean allowsRanged;
	@SerializedName("Allows Other")
	protected Boolean allowsOther;
	@SerializedName("Allowed Damage Type")
	protected String allowedDamageType;

	public DamageSourceCondition(boolean allowsMelee, boolean allowsRanged, boolean allowsOther, String allowedDamageType) {
		super();
		this.allowsMelee = allowsMelee;
		this.allowsRanged = allowsRanged;
		this.allowsOther = allowsOther;
		this.allowedDamageType = allowedDamageType;
	}
	
	@Override
	public boolean testCondition(@Nullable IEffectCallback callback, EntityPlayer playerSource, EntityLivingBase effectTarget) {
		if(!(callback instanceof GenericEventCallback)) return false;
		try {
			Event event = ((GenericEventCallback<?>) callback).getEvent();
			//Works for LivingAttack, LivingHurt, LivingDamage, DDD GatherDefenses, DDD DetermineDamage
			Method method = event.getClass().getMethod("getSource");
			DamageSource source = (DamageSource) method.invoke(event); // Invoke event.getSource()
			if(source == null) return false;

			if(!this.allowedDamageType.isEmpty() && source.getDamageType().equals(allowedDamageType)) return true;

			if (this.allowsMelee && isDamageSourceMelee(source)) return true;
			else if (this.allowsRanged && isDamageSourceRanged(source)) return true;
			else return this.allowsOther;
		} catch (Exception exception) {
			return false;
		}
    }
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * Allows Melee: optional, default true
	 * Allows Ranged: optional, default true
	 * Allows Other: optional, default true
	 * Allowed Damage Type: optional, default empty
	 */
	@Override
	public boolean validate() {
		if(this.allowsMelee == null) this.allowsMelee = true;
		if(this.allowsRanged == null) this.allowsRanged = true;
		if(this.allowsOther == null) this.allowsOther = true;
		if(this.allowedDamageType == null) this.allowedDamageType = "";

		return true;
	}

	public static boolean isDamageSourceMelee(DamageSource source) {
		return source.getImmediateSource() instanceof EntityLivingBase;
	}

	public static boolean isDamageSourceRanged(DamageSource source) {
		return !(source.getImmediateSource() instanceof EntityLivingBase) && source.getTrueSource() instanceof EntityLivingBase;
	}
}