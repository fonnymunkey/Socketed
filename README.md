# Socketed
Socketed is a minecraft mod that adds simple gem socketing into weapons and armor to provide various boosts and effects


### System

Item has Sockets

Sockets have Tiers

Gems have Tiers

--> Can go in socket of the gem tier or higher


Gems have Effects

Effects can be either AttributeEffects or ActivatableEffects

AttributeEffects just get applied to the player statically

ActivatableEffects have
- an Activator
- a list of Targets that will be affected
- a function performEffect which will do smth with the effectTarget

Activators
- have a condition they test before they activate
- when activators activate, the call effect.affectTargets on all the targets of the effect

Targets
- have a condition whether they will have the effect performed on that target
- will call target.affectTarget

Timeline
- event happens
- activator.attemptActivation(effect) 
  - after checking condition.testCondition
- calls effect.affectTargets(target)
- calls target.affectTarget(effect) 
  - for all targets of the effect (effect.affectTargets is hardcoded in ActivatableGemEffect)
- calls effect.performEffect(target.entity)
  - after checking condition.testCondition for that target entity
- effect.performEffect performs the effect on the target