package com.aether.api;

import com.aether.Aether;
import com.aether.component.MoaGenes;
import com.aether.world.dimension.AetherDimension;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Function4;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import java.util.*;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public class MoaAPI {

    public static final ResourceLocation FALLBACK_ID = Aether.locate("fallback");
    public static final Race FALLBACK_MOA = new Race(FALLBACK_ID, Aether.locate("textures/entity/moas/highlands/blue.png"), MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT);

    private static final Object2ObjectOpenHashMap<ResourceLocation, Race> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<ResourceKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final List<MatingEntry> MOA_BREEDING_REGISTRY = new ArrayList<>();

    public static void init() {
        //  Highlands
        final Race highlandsBlue = registerForBiome("highlands_blue", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED, 50, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/blue.png");
        final Race goldenrod = registerForBiome("goldenrod", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.JUMPING_STRENGTH, SpawnStatWeighting.ENDURANCE, 10, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/goldenrod.png");
        final Race mintgrass = registerForBiome("mintgrass", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.GLIDING_SPEED, SpawnStatWeighting.SPEED, 40, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/mintgrass.png");

        final Race tangerine = registerForBiome("tangerine", AetherDimension.HIGHLANDS_FOREST, MoaAttributes.JUMPING_STRENGTH, SpawnStatWeighting.SPEED, 15, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/tangerine.png");
        appendBiome(AetherDimension.HIGHLANDS_FOREST, highlandsBlue.id, 50);
        appendBiome(AetherDimension.HIGHLANDS_FOREST, goldenrod.id, 35);

        final Race foxtrot = registerForBiome("foxtrot", AetherDimension.HIGHLANDS_THICKET, MoaAttributes.DROP_MULTIPLIER, SpawnStatWeighting.TANK, 5, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/foxtrot.png");
        appendBiome(AetherDimension.HIGHLANDS_THICKET, tangerine.id, 50);
        appendBiome(AetherDimension.HIGHLANDS_THICKET, mintgrass.id, 45);

        final Race strawberryWistar = registerForBiome("strawberry_wistar", AetherDimension.WISTERIA_WOODS, MoaAttributes.GLIDING_DECAY, SpawnStatWeighting.SPEED, 49, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/strawberry_wistar.png");
        final Race scarlet = registerForBiome("scarlet", AetherDimension.WISTERIA_WOODS, MoaAttributes.GLIDING_SPEED, SpawnStatWeighting.ENDURANCE, 2, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/scarlet.png");
        appendBiome(AetherDimension.WISTERIA_WOODS, goldenrod.id, 49);


        //  Breeding
        final Race moonstruck = registerForBreedingPredicate("moonstruck", mintgrass, strawberryWistar, MoaAttributes.GLIDING_SPEED, SpawnStatWeighting.SPEED, true, true, ParticleTypes.END_ROD, "textures/entity/moas/highlands/moonstruck.png", ((moaGenes, moaGenes2, world, pos) -> world.isNight() && world.getRandom().nextFloat() <= 0.1F));
    }

    public static Race registerForBiome(String name, ResourceKey<Biome> spawnBiome, MoaAttributes affinity, SpawnStatWeighting spawnStats, int weight, boolean glowing, boolean legendary, ParticleType<?> particles, String texturePath) {
        ResourceLocation raceId = Aether.locate(name);
        ResourceLocation texture = Aether.locate(texturePath);

        final Race race = new Race(raceId, texture, affinity, spawnStats, glowing, legendary, particles);
        MOA_RACE_REGISTRY.put(raceId, race);
        appendBiome(spawnBiome, raceId, weight);
        return race;
    }

    public static Race registerForBreedingChance(String name, Race parentA, Race parentB, MoaAttributes affinity, SpawnStatWeighting spawnStats, float chance, boolean glowing, boolean legendary, ParticleType<?> particles, String texturePath) {
        return registerForBreedingPredicate(name, parentA, parentB, affinity, spawnStats, glowing, legendary, particles, texturePath, createChanceCheck(chance));
    }

    public static Race registerForBreedingPredicate(String name, Race parentA, Race parentB, MoaAttributes affinity, SpawnStatWeighting spawnStats, boolean glowing, boolean legendary, ParticleType<?> particles, String texturePath, Function4<MoaGenes, MoaGenes, Level, BlockPos, Boolean> breedingPredicate) {
        ResourceLocation raceId = Aether.locate(name);
        ResourceLocation texture = Aether.locate(texturePath);

        final Race race = new Race(raceId, texture, affinity, spawnStats, glowing, legendary, particles);
        MOA_RACE_REGISTRY.put(raceId, race);
        appendBreeding(new MatingEntry(raceId, createIdentityCheck(parentA, parentB), breedingPredicate));
        return race;
    }

    public static void appendBiome(ResourceKey<Biome> spawnBiome, ResourceLocation raceId, int weight) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(raceId, weight);
    }

    public static void appendBreeding(MatingEntry entry) {
        MOA_BREEDING_REGISTRY.add(entry);
    }

    public static Race getRace(ResourceLocation raceId) {
        return MOA_RACE_REGISTRY.getOrDefault(raceId, FALLBACK_MOA);
    }

    public static Iterator<ResourceLocation> getRegisteredRaces() {
        return MOA_RACE_REGISTRY.keySet().iterator();
    }

    public static Optional<SpawnBucket> getSpawnBucket(ResourceKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }

    public static Race getMoaForBiome(ResourceKey<Biome> biome, Random random) {
        Optional<ResourceLocation> raceOptional =
                Optional.ofNullable(getSpawnBucket(biome)
                .map(bucket -> bucket.get(random))
                .orElse(MOA_SPAWN_REGISTRY.get(AetherDimension.HIGHLANDS_PLAINS).get(random)));
        return raceOptional.map(MoaAPI::getRace).orElse(FALLBACK_MOA);
    }

    public static Race getMoaForBreeding(MoaGenes parentA, MoaGenes parentB, Level world, BlockPos pos) {
        var childRace =
                MOA_BREEDING_REGISTRY.stream()
                .filter(matingEntry -> matingEntry.identityCheck.test(parentA.getRace(), parentB.getRace()) && matingEntry.additionalChecks.apply(parentA, parentB, world, pos))
                .findAny();
        return childRace.map(MatingEntry::get).orElse(world.getRandom().nextBoolean() ? parentA.getRace() : parentB.getRace());
    }

    @Environment(EnvType.CLIENT)
    public static String formatForTranslation(ResourceLocation raceId) {
        return "moa.race." + raceId.getPath();
    }

    public static record Race(ResourceLocation id, ResourceLocation texturePath, MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting, boolean glowing, boolean legendary, ParticleType<?> particles) {
    }

    private static record SpawnBucketEntry(ResourceLocation id, int weight) {
        public boolean test(Random random, int whole) {
            return random.nextInt(whole) < weight;
        }
    }

    private static class SpawnBucket {

        private final List<SpawnBucketEntry> entries = new ArrayList<>();
        private SpawnBucketEntry heaviest = new SpawnBucketEntry(FALLBACK_ID, 0);
        private int totalWeight;

        public void put(ResourceLocation raceId, int weight) {

            if(weight < 1) {
                throw new IllegalArgumentException(raceId.toString() + " has an invalid weight, must be 1 or higher!");
            }

            SpawnBucketEntry entry = new SpawnBucketEntry(raceId, weight);

            if(weight > heaviest.weight) {
                heaviest = entry;
            }

            entries.add(entry);
            totalWeight += weight;
        }

        public ResourceLocation get(Random random) {
            if(entries.size() == 1) {
                return entries.get(0).id;
            }
            Collections.shuffle(entries);
            Optional<SpawnBucketEntry> entryOptional = entries.stream().filter(entry -> entry.test(random, totalWeight)).findFirst();
            return entryOptional.map(SpawnBucketEntry::id).orElse(heaviest.id);
        }

    }

    private static record MatingEntry(ResourceLocation race, BiPredicate<Race, Race> identityCheck, Function4<MoaGenes, MoaGenes, Level, BlockPos, Boolean> additionalChecks) {
        public Race get() {
            return getRace(race);
        }
    }

    public static BiPredicate<Race, Race> createIdentityCheck(Race raceA, Race raceB) {
        return (parentA, parentB) -> (raceA == parentA && raceB == parentB) || (raceB == parentA && raceA == parentB);
    }

    public static Function4<MoaGenes, MoaGenes, Level, BlockPos, Boolean> createChanceCheck(float chance) {
        return (parentA, parentB, world, pos) -> world.getRandom().nextFloat() < chance;
    }

    private static record SpawnStatData(float base, float variance) {}

    public enum SpawnStatWeighting {
        SPEED(0.08F, 0.1F, 0.02F, 0.03F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.04F, -0.08F, 0F, -0.01F, 0, 6),
        ENDURANCE(0.023F, 0.06F, 0.02F, 0.02F, -0.085F, -0.08F, -0.01F, -0.02F, 2, 8),
        TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.02F, -0.01F, 6, 6),
        MYTHICAL_SPEED(0.31F, 0.17F, 0.082F, 0.0375F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        MYTHICAL_GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.085F, -0.085F, 0F, -0.01F, 0, 6),
        MYTHICAL_TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.03F, -0.01F, 14, 6),
        MYTHICAL_ALL(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, -0.085F, -0.03F, -0.01F, 14, 6),;

        public final ImmutableMap<MoaAttributes, SpawnStatData> data;

        SpawnStatWeighting(float baseGroundSpeed, float groundSpeedVariance, float baseGlidingSpeed, float glidingSpeedVariance, float baseGlidingDecay, float glidingDecayVariance, float baseJumpStrength, float jumpStrengthVariance, float baseMaxHealth, float maxHealthVariance) {
            Builder builder = ImmutableMap.<MoaAttributes, SpawnStatData>builder();
            builder.put(MoaAttributes.GROUND_SPEED, new SpawnStatData(baseGroundSpeed, groundSpeedVariance));
            builder.put(MoaAttributes.GLIDING_SPEED, new SpawnStatData(baseGlidingSpeed, glidingSpeedVariance));
            builder.put(MoaAttributes.GLIDING_DECAY, new SpawnStatData(baseGlidingDecay, glidingDecayVariance));
            builder.put(MoaAttributes.JUMPING_STRENGTH, new SpawnStatData(baseJumpStrength, jumpStrengthVariance));
            builder.put(MoaAttributes.MAX_HEALTH, new SpawnStatData(baseMaxHealth, maxHealthVariance));
            builder.put(MoaAttributes.DROP_MULTIPLIER, new SpawnStatData(0, 0));
            data = builder.build();
        }

        public float configure(MoaAttributes attribute, Race race, Random random) {
            SpawnStatData statData = data.get(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base + (random.nextFloat() * statData.variance) * (race.defaultAffinity == attribute ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F) : 1F)));
        }
    }
}
