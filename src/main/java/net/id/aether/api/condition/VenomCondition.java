package net.id.aether.api.condition;

import net.id.aether.Aether;
import net.id.aether.tag.AetherEntityTypeTags;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class VenomCondition extends ConditionProcessor {

    public VenomCondition(Identifier id) {
        super(id, AetherEntityTypeTags.VENOM_IMMUNITY, 300, 300, 0.5F, 0.025F,400, 0.05F);
    }

    @Override
    public void process(World world, LivingEntity entity, Severity severity, float rawSeverity) {
        if(rawSeverity > visThreshold && world.getTime() % 20 == 0) {
            var poisonEffect = switch (severity) {
                case MILD -> new StatusEffectInstance(StatusEffects.POISON, 100, 1, true, false, true);
                case ACUTE -> new StatusEffectInstance(StatusEffects.POISON, 100, 2, true, false, true);
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.POISON, 200, 2, true, false, true);
                default -> new StatusEffectInstance(StatusEffects.POISON, 100, 0, true, false, true);
            };
            var witherEffect = switch (severity) {
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.WITHER, 100, 1, true, false, true);
                default -> null;
            };

            entity.addStatusEffect(poisonEffect);
            if(witherEffect != null) {
                entity.addStatusEffect(witherEffect);
            }
        }
    }

    @Override
    public void processPlayer(World world, PlayerEntity player, Severity severity, float rawSeverity) {
        if(rawSeverity > visThreshold && world.getTime() % 20 == 0) {
            var poisonEffect = switch (severity) {
                case MILD -> new StatusEffectInstance(StatusEffects.POISON, 100, 1, true, false, true);
                case ACUTE -> new StatusEffectInstance(StatusEffects.POISON, 200, 1, true, false, true);
                case DIRE, EXTREME -> new StatusEffectInstance(StatusEffects.POISON, 200, 2, true, false, true);
                default -> new StatusEffectInstance(StatusEffects.POISON, 100, 0, true, false, true);
            };
            var witherEffect = switch (severity) {
                case DIRE -> new StatusEffectInstance(StatusEffects.WITHER, 100, 0, true, false, true);
                case EXTREME -> new StatusEffectInstance(StatusEffects.WITHER, 200, 2, true, false, true);
                default -> null;
            };

            player.addStatusEffect(poisonEffect);
            if(witherEffect != null) {
                player.addStatusEffect(witherEffect);
            }
        }
    }

    @Override
    public void processClient(ClientWorld world, LivingEntity entity, Severity severity, float rawSeverity) {

    }
}
