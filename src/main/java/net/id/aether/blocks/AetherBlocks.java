package net.id.aether.blocks;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.id.aether.blocks.decorative.*;
import net.id.aether.blocks.mechanical.FoodBowlBlock;
import net.id.aether.blocks.mechanical.IncubatorBlock;
import net.id.aether.blocks.natural.AetherGrassBlock;
import net.id.aether.blocks.natural.AetherSaplingBlock;
import net.id.aether.blocks.natural.SwetDropBlock;
import net.id.aether.blocks.natural.aercloud.*;
import net.id.aether.blocks.natural.crop.*;
import net.id.aether.blocks.natural.plant.*;
import net.id.aether.blocks.natural.tree.*;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.AetherItems;
import net.id.aether.registry.AetherRegistryQueues;
import net.id.aether.util.AetherSignType;
import net.id.aether.util.RenderUtils;
import net.id.aether.world.feature.tree.*;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlockActions.*;
import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.AbstractBlock.Settings.of;
import static net.minecraft.block.Blocks.*;

@SuppressWarnings("unused")
public class AetherBlocks {

    protected static Settings unbreakable(AbstractBlock.Settings settings) { return settings.strength(-1f, 3600000f); }

    // Grass Blocks
    private static Settings grassBlock() { return copy(GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f); }
    public static final AetherGrassBlock AETHER_GRASS_BLOCK = add("aether_grass", new AetherGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable());
    public static final AetherGrassBlock AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", new AetherGrassBlock(grassBlock().mapColor(MapColor.GOLD)));
    // Soil Blocks
    public static final Block AETHER_DIRT = add("aether_dirt", new Block(copy(DIRT).strength(0.3f)), tillable(), flattenable());
    public static final FarmlandBlock AETHER_FARMLAND = add("aether_farmland", new AetherFarmlandBlock(copy(FARMLAND)));
    public static final AetherDirtPathBlock AETHER_DIRT_PATH = add("aether_grass_path", new AetherDirtPathBlock(copy(DIRT_PATH)));
    public static final Block QUICKSOIL = add("quicksoil", new Block(of(Material.AGGREGATE).strength(0.5f, -1f).slipperiness(1F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)));
    // Glass Blocks
    private static Settings quicksoilGlass() { return copy(GLASS).strength(0.2f, -1f).slipperiness(1f).velocityMultiplier(1.102f).luminance(state -> 14); }
    public static final GlassBlock QUICKSOIL_GLASS = add("quicksoil_glass", new GlassBlock(quicksoilGlass()), translucentRenderLayer);
    public static final PaneBlock QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", new AetherPaneBlock(quicksoilGlass()), translucentRenderLayer);
    // Aerclouds
    private static Settings aercloud() { return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never); }
    public static final AercloudBlock COLD_AERCLOUD = add("cold_aercloud", new AercloudBlock(aercloud().mapColor(MapColor.WHITE)), translucentRenderLayer);
    public static final BlueAercloudBlock BLUE_AERCLOUD = add("blue_aercloud", new BlueAercloudBlock(aercloud().mapColor(MapColor.LIGHT_BLUE)), translucentRenderLayer);
    public static final PinkAercloudBlock PINK_AERCLOUD = add("pink_aercloud", new PinkAercloudBlock(aercloud().mapColor(MapColor.PINK)), translucentRenderLayer);
    public static final GoldenAercloudBlock GOLDEN_AERCLOUD = add("golden_aercloud", new GoldenAercloudBlock(aercloud().mapColor(MapColor.GOLD)), translucentRenderLayer);
    // Fluids
    public static final FluidBlock DENSE_AERCLOUD = add("dense_aercloud", new FluidBlock(AetherFluids.DENSE_AERCLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});
    // Organic Extra
    public static final Block ICESTONE = add("icestone", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block AEROGEL = add("aerogel", new Block(of(Material.SOIL).strength(1f, 1200f).sounds(BlockSoundGroup.GLASS).solidBlock(never).nonOpaque()), translucentRenderLayer);
    // Smooth Holystone
    private static Settings holystone() { return of(Material.STONE, MapColor.WHITE_GRAY).requiresTool().strength(0.5f, 1f).sounds(BlockSoundGroup.STONE); }
    public static final Block HOLYSTONE = add("holystone", new Block(holystone()));
    public static final SlabBlock HOLYSTONE_SLAB = add("holystone_slab", new SlabBlock(holystone()));
    public static final AetherStairsBlock HOLYSTONE_STAIRS = add("holystone_stairs", new AetherStairsBlock(HOLYSTONE.getDefaultState(), holystone()));
    public static final WallBlock HOLYSTONE_WALL = add("holystone_wall", new WallBlock(holystone()));
    // Cobbled Holystone
    private static Settings cobbledHolystone() { return holystone().strength(0.4f, 8f); }
    public static final Block COBBLED_HOLYSTONE = add("cobbled_holystone", new Block(cobbledHolystone()));
    public static final SlabBlock COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", new SlabBlock(cobbledHolystone()));
    public static final AetherStairsBlock COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", new AetherStairsBlock(COBBLED_HOLYSTONE.getDefaultState(), cobbledHolystone()));
    public static final WallBlock COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", new WallBlock(cobbledHolystone()));
    // Mossy Holystone
    private static Settings mossyCobbledHolystone() { return cobbledHolystone().mapColor(MapColor.PALE_GREEN); }
    public static final Block MOSSY_HOLYSTONE = add("mossy_holystone", new Block(mossyCobbledHolystone()));
    public static final Block GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", new Block(mossyCobbledHolystone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final SlabBlock MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", new SlabBlock(mossyCobbledHolystone()));
    public static final AetherStairsBlock MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", new AetherStairsBlock(MOSSY_HOLYSTONE.getDefaultState(), mossyCobbledHolystone()));
    public static final WallBlock MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", new WallBlock(mossyCobbledHolystone()));
    // Holystone Bricks
    private static Settings holystoneBrick() { return holystone().strength(1.5f, 6f); }
    public static final Block HOLYSTONE_BRICK = add("holystone_brick", new Block(holystoneBrick()));
    public static final SlabBlock HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", new SlabBlock(holystoneBrick()));
    public static final AetherStairsBlock HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", new AetherStairsBlock(HOLYSTONE_BRICK.getDefaultState(), holystoneBrick()));
    public static final WallBlock HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", new WallBlock(holystoneBrick()));
    // Dungeon Blocks
    /*
        private static Settings angelicStone() { return of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE); }
        public static final Block ANGELIC_STONE = add("angelic_stone", new Block(angelicStone()));
        public static final Block ANGELIC_CRACKED_STONE = add("angelic_stone_cracked", new Block(angelicStone()));
        public static final SlabBlock ANGELIC_SLAB = add("angelic_slab", new SlabBlock(angelicStone()));
        public static final AetherStairsBlock ANGELIC_STAIRS = add("angelic_stairs", new AetherStairsBlock(ANGELIC_STONE.getDefaultState(), angelicStone()));
        public static final WallBlock ANGELIC_WALL = add("angelic_wall", new WallBlock(angelicStone()));

        private static Settings lightAngelicStone() { return angelicStone().luminance(state -> 11); }
        public static final Block LIGHT_ANGELIC_STONE = add("light_angelic_stone", new Block(lightAngelicStone()));
        public static final Block LIGHT_ANGELIC_STONE_TRAP = add("light_angelic_stone_trap", new Block(unbreakable(lightAngelicStone())));
        public static final SlabBlock LIGHT_ANGELIC_SLAB = add("light_angelic_slab", new SlabBlock(lightAngelicStone()));
        public static final AetherStairsBlock LIGHT_ANGELIC_STAIRS = add("light_angelic_stairs", new AetherStairsBlock(LIGHT_ANGELIC_STONE.getDefaultState(), lightAngelicStone()));
        public static final WallBlock LIGHT_ANGELIC_WALL = add("light_angelic_wall", new WallBlock(lightAngelicStone()));

        private static Settings hellfireStone() { return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE); }
        public static final Block HELLFIRE_STONE = add("hellfire_stone", new Block(hellfireStone()));
        public static final Block HELLFIRE_CRACKED_STONE = add("hellfire_stone_cracked", new Block(hellfireStone()));
        public static final Block HELLFIRE_STONE_TRAP = add("hellfire_stone_trap", new Block(unbreakable(hellfireStone())));
        public static final SlabBlock HELLFIRE_SLAB = add("hellfire_slab", new SlabBlock(hellfireStone()));
        public static final AetherStairsBlock HELLFIRE_STAIRS = add("hellfire_stairs", new AetherStairsBlock(HELLFIRE_STONE.getDefaultState(), hellfireStone()));
        public static final WallBlock HELLFIRE_WALL = add("hellfire_wall", new WallBlock(hellfireStone()));

        private static Settings lightHellfireStone() { return hellfireStone(); }
        public static final Block LIGHT_HELLFIRE_STONE = add("light_hellfire_stone", new Block(lightHellfireStone()));
        public static final Block LIGHT_HELLFIRE_STONE_TRAP = add("light_hellfire_stone_trap", new Block(unbreakable(lightHellfireStone())));
        public static final SlabBlock LIGHT_HELLFIRE_SLAB = add("light_hellfire_slab", new SlabBlock(lightHellfireStone()));
        public static final AetherStairsBlock LIGHT_HELLFIRE_STAIRS = add("light_hellfire_stairs", new AetherStairsBlock(LIGHT_HELLFIRE_STONE.getDefaultState(), lightHellfireStone()));
        public static final WallBlock LIGHT_HELLFIRE_WALL = add("light_hellfire_wall", new WallBlock(lightHellfireStone()));
    */
    private static Settings carvedStone() { return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE); }
    public static final Block CARVED_STONE = add("carved_stone", new Block(carvedStone()));
    public static final SlabBlock CARVED_STONE_SLAB = add("carved_stone_slab", new SlabBlock(carvedStone()));
    public static final AetherStairsBlock CARVED_STONE_STAIRS = add("carved_stone_stairs", new AetherStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final WallBlock CARVED_STONE_WALL = add("carved_stone_wall", new WallBlock(carvedStone()));

    public static final Block MOSSY_CARVED_STONE = add("mossy_carved_stone", new Block(carvedStone()));
    public static final SlabBlock MOSSY_CARVED_STONE_SLAB = add("mossy_carved_stone_slab", new SlabBlock(carvedStone()));
    public static final AetherStairsBlock MOSSY_CARVED_STONE_STAIRS = add("mossy_carved_stone_stairs", new AetherStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final WallBlock MOSSY_CARVED_STONE_WALL = add("mossy_carved_stone_wall", new WallBlock(carvedStone()));

    public static final Block CRACKED_CARVED_STONE = add("cracked_carved_stone", new Block(carvedStone()));
    public static final Block GLYPHED_CARVED_STONE = add("glyphed_carved_stone", new Block(carvedStone().luminance(value -> 3)));
    public static final Block CARVED_STONE_PANEL = add("carved_stone_panel", new Block(carvedStone()));
    public static final Block CARVED_STONE_PANEL_LIT = add("carved_stone_panel_lit", new Block(carvedStone().luminance(value -> 12)));
    public static final Block CARVED_STONE_EYE = add("carved_stone_eye", new Block(carvedStone()));
    public static final Block CARVED_STONE_EYE_LIT = add("carved_stone_eye_lit", new Block(carvedStone().luminance(value -> 8)));
    /*
        private static Settings lightCarvedStone() { return carvedStone().luminance(state -> 11); }
        public static final Block LIGHT_CARVED_STONE = add("light_carved_stone", new Block(lightCarvedStone()));
        public static final Block LIGHT_CARVED_STONE_TRAP = add("light_carved_stone_trap", new Block(unbreakable(lightCarvedStone())));
        public static final SlabBlock LIGHT_CARVED_SLAB = add("light_carved_slab", new SlabBlock(lightCarvedStone()));
        public static final AetherStairsBlock LIGHT_CARVED_STAIRS = add("light_carved_stairs", new AetherStairsBlock(LIGHT_CARVED_STONE.getDefaultState(), lightCarvedStone()));
        public static final WallBlock LIGHT_CARVED_WALL = add("light_carved_wall", new WallBlock(lightCarvedStone()));

        private static Settings sentryStone() { return of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE); }
        public static final Block SENTRY_STONE = add("sentry_stone", new Block(sentryStone()));
        public static final Block SENTRY_CRACKED_STONE = add("sentry_stone_cracked", new Block(sentryStone()));
        public static final Block SENTRY_STONE_TRAP = add("sentry_stone_trap", new Block(unbreakable(sentryStone())));
        public static final SlabBlock SENTRY_SLAB = add("sentry_slab", new SlabBlock(sentryStone()));
        public static final AetherStairsBlock SENTRY_STAIRS = add("sentry_stairs", new AetherStairsBlock(SENTRY_STONE.getDefaultState(), sentryStone()));
        public static final WallBlock SENTRY_WALL = add("sentry_wall", new WallBlock(sentryStone()));

        private static Settings lightSentryStone() { return sentryStone().luminance(state -> 10); }
        public static final Block LIGHT_SENTRY_STONE = add("light_sentry_stone", new Block(lightSentryStone()));
        public static final SlabBlock LIGHT_SENTRY_SLAB = add("light_sentry_slab", new SlabBlock(lightSentryStone()));
        public static final AetherStairsBlock LIGHT_SENTRY_STAIRS = add("light_sentry_stairs", new AetherStairsBlock(LIGHT_SENTRY_STONE.getDefaultState(), lightSentryStone()));
        public static final WallBlock LIGHT_SENTRY_WALL = add("light_sentry_wall", new WallBlock(lightSentryStone()));
    */
    protected static Settings flowerPot() { return copy(POTTED_OAK_SAPLING); }

    // Skyroot Wood
    private static final WoodTypeFactory skyroot = new WoodTypeFactory(MapColor.GREEN, MapColor.TERRACOTTA_GREEN);
    public static final SaplingBlock SKYROOT_SAPLING = add("skyroot_sapling", new AetherSaplingBlock(new SkyrootSaplingGenerator(), skyroot.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_SKYROOT_SAPLING = add("potted_skyroot_sapling", new FlowerPotBlock(SKYROOT_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock SKYROOT_LOG = add("skyroot_log", new PillarBlock(skyroot.log()), flammableLog);
    public static final PillarBlock SKYROOT_WOOD = add("skyroot_wood", new PillarBlock(skyroot.wood()), flammableLog);
    public static final PillarBlock STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", new PillarBlock(skyroot.strippedLog()), flammableLog, strippedFrom(SKYROOT_LOG));
    public static final PillarBlock STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", new PillarBlock(skyroot.strippedWood()), flammableLog, strippedFrom(SKYROOT_WOOD));
    public static final LeavesBlock SKYROOT_LEAVES = add("skyroot_leaves", new LeavesBlock(skyroot.leaves()), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", new LeafPileBlock(skyroot.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block SKYROOT_PLANKS = add("skyroot_planks", new Block(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_BOOKSHELF = add("skyroot_bookshelf", new Block(copy(BOOKSHELF).mapColor(skyroot.plankColor())), flammable(30, 20));
    public static final FenceBlock SKYROOT_FENCE = add("skyroot_fence", new FenceBlock(skyroot.planks()), flammablePlanks);
    public static final FenceGateBlock SKYROOT_FENCE_GATE = add("skyroot_fence_gate", new FenceGateBlock(skyroot.planks()), flammablePlanks);
    public static final SlabBlock SKYROOT_SLAB = add("skyroot_slab", new SlabBlock(skyroot.planks()), flammablePlanks);
    public static final AetherStairsBlock SKYROOT_STAIRS = add("skyroot_stairs", new AetherStairsBlock(SKYROOT_PLANKS.getDefaultState(), skyroot.planks()), flammablePlanks);
    public static final AetherTrapdoorBlock SKYROOT_TRAPDOOR = add("skyroot_trapdoor", new AetherTrapdoorBlock(skyroot.trapdoor()), cutoutRenderLayer);
    public static final AetherDoorBlock SKYROOT_DOOR = add("skyroot_door", new AetherDoorBlock(skyroot.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock SKYROOT_BUTTON = add("skyroot_button", new AetherWoodenButtonBlock(skyroot.button()));
    public static final AetherPressurePlateBlock SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", new AetherPressurePlateBlock(ActivationRule.EVERYTHING, skyroot.pressurePlate()));
    public static final SignBlock SKYROOT_SIGN = add("skyroot_sign", new SignBlock(skyroot.sign(), AetherSignType.SKYROOT), signBlockEntity);
    public static final WallSignBlock SKYROOT_WALL_SIGN = add("skyroot_wall_sign", new WallSignBlock(skyroot.sign().dropsLike(SKYROOT_SIGN), AetherSignType.SKYROOT), signBlockEntity);
    // Golden Oak Wood
    private static final WoodTypeFactory goldenOak = new WoodTypeFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final SaplingBlock GOLDEN_OAK_SAPLING = add("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), goldenOak.sapling().luminance(state -> 7)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_GOLDEN_OAK_SAPLING = add("potted_golden_oak_sapling", new FlowerPotBlock(GOLDEN_OAK_SAPLING, flowerPot().luminance(state -> 7)), cutoutRenderLayer);
    public static final GoldenOakLogBlock GOLDEN_OAK_LOG = add("golden_oak_log", new GoldenOakLogBlock(goldenOak.log()), flammableLog);
    public static final PillarBlock GOLDEN_OAK_WOOD = add("golden_oak_wood", new PillarBlock(goldenOak.log()), flammableLog);
    public static final PillarBlock STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", new PillarBlock(goldenOak.strippedLog()), flammableLog, strippedFrom(GOLDEN_OAK_LOG));
    public static final PillarBlock STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", new PillarBlock(goldenOak.strippedWood()), flammableLog, strippedFrom(GOLDEN_OAK_WOOD));
    public static final AetherLeavesBlock GOLDEN_OAK_LEAVES = add("golden_oak_leaves", new AetherLeavesBlock(goldenOak.leaves().luminance((value -> 11)), true), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block GOLDEN_OAK_PLANKS = add("golden_oak_planks", new Block(goldenOak.planks()), flammablePlanks);
    public static final FenceBlock GOLDEN_OAK_FENCE = add("golden_oak_fence", new FenceBlock(goldenOak.planks()), flammablePlanks);
    public static final FenceGateBlock GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", new FenceGateBlock(goldenOak.planks()), flammablePlanks);
    public static final SlabBlock GOLDEN_OAK_SLAB = add("golden_oak_slab", new SlabBlock(goldenOak.planks()), flammablePlanks);
    public static final AetherStairsBlock GOLDEN_OAK_STAIRS = add("golden_oak_stairs", new AetherStairsBlock(GOLDEN_OAK_PLANKS.getDefaultState(), goldenOak.planks()), flammablePlanks);
    public static final AetherTrapdoorBlock GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", new AetherTrapdoorBlock(goldenOak.trapdoor()), cutoutRenderLayer);
    public static final AetherDoorBlock GOLDEN_OAK_DOOR = add("golden_oak_door", new AetherDoorBlock(goldenOak.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock GOLDEN_OAK_BUTTON = add("golden_oak_button", new AetherWoodenButtonBlock(goldenOak.button()));
    public static final AetherPressurePlateBlock GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", new AetherPressurePlateBlock(ActivationRule.EVERYTHING, goldenOak.pressurePlate()));
    public static final SignBlock GOLDEN_OAK_SIGN = add("golden_oak_sign", new SignBlock(goldenOak.sign(), AetherSignType.GOLDEN_OAK), signBlockEntity);
    public static final WallSignBlock GOLDEN_OAK_WALL_SIGN = add("golden_oak_wall_sign", new WallSignBlock(goldenOak.sign().dropsLike(GOLDEN_OAK_SIGN), AetherSignType.GOLDEN_OAK), signBlockEntity);
    // Orange Wood
    private static final WoodTypeFactory orange = new WoodTypeFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final SaplingBlock ORANGE_SAPLING = add("orange_sapling", new AetherSaplingBlock(new OrangeSaplingGenerator(), orange.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ORANGE_SAPLING = add("potted_orange_sapling", new FlowerPotBlock(ORANGE_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock ORANGE_LOG = add("orange_log", new PillarBlock(orange.log()), flammableLog);
    public static final PillarBlock ORANGE_WOOD = add("orange_wood", new PillarBlock(orange.wood()), flammableLog);
    public static final PillarBlock STRIPPED_ORANGE_LOG = add("stripped_orange_log", new PillarBlock(orange.strippedLog()), flammableLog, strippedFrom(ORANGE_LOG));
    public static final PillarBlock STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", new PillarBlock(orange.strippedWood()), flammableLog, strippedFrom(ORANGE_WOOD));
    public static final FruitingLeavesBlock ORANGE_LEAVES = add("orange_leaves", new FruitingLeavesBlock(orange.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), () -> AetherItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ORANGE_PLANKS = add("orange_planks", new Block(orange.planks()), flammablePlanks);
    public static final FenceBlock ORANGE_FENCE = add("orange_fence", new FenceBlock(orange.planks()), flammablePlanks);
    public static final FenceGateBlock ORANGE_FENCE_GATE = add("orange_fence_gate", new FenceGateBlock(orange.planks()), flammablePlanks);
    public static final SlabBlock ORANGE_SLAB = add("orange_slab", new SlabBlock(orange.planks()), flammablePlanks);
    public static final AetherStairsBlock ORANGE_STAIRS = add("orange_stairs", new AetherStairsBlock(ORANGE_PLANKS.getDefaultState(), orange.planks()), flammablePlanks);
    public static final AetherTrapdoorBlock ORANGE_TRAPDOOR = add("orange_trapdoor", new AetherTrapdoorBlock(orange.trapdoor()), cutoutRenderLayer);
    public static final AetherDoorBlock ORANGE_DOOR = add("orange_door", new AetherDoorBlock(orange.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock ORANGE_BUTTON = add("orange_button", new AetherWoodenButtonBlock(orange.button()));
    public static final AetherPressurePlateBlock ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", new AetherPressurePlateBlock(ActivationRule.EVERYTHING, orange.pressurePlate()));
    public static final SignBlock ORANGE_SIGN = add("orange_sign", new SignBlock(orange.sign(), AetherSignType.ORANGE), signBlockEntity);
    public static final WallSignBlock ORANGE_WALL_SIGN = add("orange_wall_sign", new WallSignBlock(orange.sign().dropsLike(ORANGE_SIGN), AetherSignType.ORANGE), signBlockEntity);
    // Crystal Wood
    private static final WoodTypeFactory crystal = new WoodTypeFactory(MapColor.IRON_GRAY, MapColor.LICHEN_GREEN, MapColor.LIGHT_BLUE);
    public static final SaplingBlock CRYSTAL_SAPLING = add("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), crystal.sapling().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_CRYSTAL_SAPLING = add("potted_crystal_sapling", new FlowerPotBlock(CRYSTAL_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock CRYSTAL_LOG = add("crystal_log", new PillarBlock(crystal.log()), flammableLog);
    public static final PillarBlock CRYSTAL_WOOD = add("crystal_wood", new PillarBlock(crystal.wood()), flammableLog);
    public static final PillarBlock STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", new PillarBlock(crystal.strippedLog()), flammableLog, strippedFrom(CRYSTAL_LOG));
    public static final PillarBlock STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", new PillarBlock(crystal.strippedWood()), flammableLog, strippedFrom(CRYSTAL_WOOD));
    public static final CrystalLeavesBlock CRYSTAL_LEAVES = add("crystal_leaves", new CrystalLeavesBlock(crystal.leaves().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block CRYSTAL_PLANKS = add("crystal_planks", new Block(crystal.planks()), flammablePlanks);
    public static final FenceBlock CRYSTAL_FENCE = add("crystal_fence", new FenceBlock(crystal.planks()), flammablePlanks);
    public static final FenceGateBlock CRYSTAL_FENCE_GATE = add("crystal_fence_gate", new FenceGateBlock(crystal.planks()), flammablePlanks);
    public static final SlabBlock CRYSTAL_SLAB = add("crystal_slab", new SlabBlock(crystal.planks()), flammablePlanks);
    public static final AetherStairsBlock CRYSTAL_STAIRS = add("crystal_stairs", new AetherStairsBlock(CRYSTAL_PLANKS.getDefaultState(), crystal.planks()), flammablePlanks);
    public static final AetherTrapdoorBlock CRYSTAL_TRAPDOOR = add("crystal_trapdoor", new AetherTrapdoorBlock(crystal.trapdoor()), cutoutRenderLayer);
    public static final AetherDoorBlock CRYSTAL_DOOR = add("crystal_door", new AetherDoorBlock(crystal.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock CRYSTAL_BUTTON = add("crystal_button", new AetherWoodenButtonBlock(crystal.button()));
    public static final AetherPressurePlateBlock CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", new AetherPressurePlateBlock(ActivationRule.EVERYTHING, crystal.pressurePlate()));
    public static final SignBlock CRYSTAL_SIGN = add("crystal_sign", new SignBlock(crystal.sign(), AetherSignType.CRYSTAL), signBlockEntity);
    public static final WallSignBlock CRYSTAL_WALL_SIGN = add("crystal_wall_sign", new WallSignBlock(crystal.wallSign().dropsLike(CRYSTAL_SIGN), AetherSignType.CRYSTAL), signBlockEntity);
    // Wisteria Wood
    private static final WoodTypeFactory wisteria = new WoodTypeFactory(MapColor.PALE_YELLOW, MapColor.BROWN);
    public static final PillarBlock WISTERIA_LOG = add("wisteria_log", new PillarBlock(wisteria.log()), flammableLog);
    public static final PillarBlock WISTERIA_WOOD = add("wisteria_wood", new PillarBlock(wisteria.wood()), flammableLog);
    public static final PillarBlock STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", new PillarBlock(wisteria.strippedLog()), flammableLog, strippedFrom(WISTERIA_LOG));
    public static final PillarBlock STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", new PillarBlock(wisteria.strippedWood()), flammableLog, strippedFrom(WISTERIA_WOOD));
    public static final Block WISTERIA_PLANKS = add("wisteria_planks", new Block(wisteria.planks()), flammablePlanks);
    public static final FenceBlock WISTERIA_FENCE = add("wisteria_fence", new FenceBlock(wisteria.planks()), flammablePlanks);
    public static final FenceGateBlock WISTERIA_FENCE_GATE = add("wisteria_fence_gate", new FenceGateBlock(wisteria.planks()), flammablePlanks);
    public static final SlabBlock WISTERIA_SLAB = add("wisteria_slab", new SlabBlock(wisteria.planks()), flammablePlanks);
    public static final AetherStairsBlock WISTERIA_STAIRS = add("wisteria_stairs", new AetherStairsBlock(WISTERIA_PLANKS.getDefaultState(), wisteria.planks()), flammablePlanks);
    public static final AetherTrapdoorBlock WISTERIA_TRAPDOOR = add("wisteria_trapdoor", new AetherTrapdoorBlock(wisteria.trapdoor()), cutoutRenderLayer);
    public static final AetherDoorBlock WISTERIA_DOOR = add("wisteria_door", new AetherDoorBlock(wisteria.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock WISTERIA_BUTTON = add("wisteria_button", new AetherWoodenButtonBlock(wisteria.button()));
    public static final AetherPressurePlateBlock WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", new AetherPressurePlateBlock(ActivationRule.EVERYTHING, wisteria.pressurePlate()));
    public static final SignBlock WISTERIA_SIGN = add("wisteria_sign", new SignBlock(wisteria.sign(), AetherSignType.WISTERIA), signBlockEntity);
    public static final WallSignBlock WISTERIA_WALL_SIGN = add("wisteria_wall_sign", new WallSignBlock(wisteria.wallSign().dropsLike(WISTERIA_SIGN), AetherSignType.WISTERIA), signBlockEntity);

    private static final WoodTypeFactory roseWisteria = wisteria.withLeafColor(MapColor.PINK);
    public static final AetherLeavesBlock ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new AetherLeavesBlock(roseWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new LeafPileBlock(roseWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new AetherHangerBlock(roseWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory frostWisteria = wisteria.withLeafColor(MapColor.LIGHT_BLUE);
    public static final AetherLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new AetherLeavesBlock(frostWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new LeafPileBlock(frostWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new AetherHangerBlock(frostWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory lavenderWisteria = wisteria.withLeafColor(MapColor.MAGENTA);
    public static final AetherLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new AetherLeavesBlock(lavenderWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new LeafPileBlock(lavenderWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new AetherHangerBlock(lavenderWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory borealWisteria = wisteria.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] borealWisteriaColors = new Vec3i[]{RenderUtils.toRGB(0x42cdff), RenderUtils.toRGB(0x3dffcb), RenderUtils.toRGB(0xac4dff), RenderUtils.toRGB(0xff5cd6)};
    public static final AuralLeavesBlock BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteria.auralWisteriaLeaves(), false, borealWisteriaColors), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteria.sapling().luminance(state -> 5)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)), cutoutRenderLayer);
    public static final AuralHangerBlock BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteria.auralHanger(), borealWisteriaColors), flammableLeaves, cutoutRenderLayer);
    // Grasses
    private static Settings shrub() { return copy(GRASS).mapColor(MapColor.PALE_GREEN); }
    public static final AetherBrushBlock AETHER_GRASS = add("aether_grass_plant", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_GRASS_FLOWERING = add("aether_grass_flowering", new AetherBrushBlock(shrub().mapColor(MapColor.WHITE)), flammablePlant, cutoutRenderLayer);
    public static final TallPlantBlock AETHER_TALL_GRASS = add("aether_tall_grass", new TallPlantBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_FERN = add("aether_fern", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_AETHER_FERN = add("potted_aether_fern", new FlowerPotBlock(AETHER_FERN, flowerPot()), cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_BUSH = add("aether_bush", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock FLUTEGRASS = add("flutegrass", new AetherBrushBlock(shrub().mapColor(MapColor.GOLD), ImmutableSet.of(QUICKSOIL), true), flammablePlant, cutoutRenderLayer);
    public static final TallWaterPlantBlock HONEY_NETTLE = add("honey_nettle", new TallWaterPlantBlock(shrub().sounds(BlockSoundGroup.SMALL_DRIPLEAF)), cutoutRenderLayer);
    public static final AetherSeagrassBlock HALOPHIA = add("halophia", new AetherSeagrassBlock(shrub().sounds(BlockSoundGroup.WET_GRASS)), cutoutRenderLayer);
    public static final GiantLilypadBlock GIANT_LILY = add("giant_lily", new GiantLilypadBlock(copy(LILY_PAD).mapColor(MapColor.PALE_GREEN)), cutoutMippedRenderLayer);
    public static final WeepingCloudburstBlock WEEPING_CLOUDBURST = add("weeping_cloudburst", new WeepingCloudburstBlock(copy(SUGAR_CANE).mapColor(MapColor.PALE_PURPLE)), cutoutMippedRenderLayer);
    public static final MossStarBlock MOSS_STAR = add("moss_star", new MossStarBlock(copy(MOSS_BLOCK).dynamicBounds().luminance(state -> state.get(Properties.WATERLOGGED) ? 5 : 0)), cutoutRenderLayer);
    public static final MossBallBlock MOSS_BALL = add("moss_ball", new MossBallBlock(copy(MOSS_BLOCK).dynamicBounds()), flammablePlant);

    private static Settings lichen() { return copy(OAK_WOOD).mapColor(MapColor.DULL_RED).sounds(BlockSoundGroup.MOSS_BLOCK); }
    public static final LichenBlock LICHEN = add("lichen", new LichenBlock(lichen().ticksRandomly(), false));
    public static final LichenPileBlock LICHEN_PILE = add("lichen_pile", new LichenPileBlock(lichen(), false));
    public static final LichenBlock LUCATIEL_LICHEN = add("lucatiel_lichen", new LichenBlock(lichen().ticksRandomly(), true));
    public static final LichenPileBlock LUCATIEL_LICHEN_PILE = add("lucatiel_lichen_pile", new LichenPileBlock(lichen(), true));

    public static final AmadrysCropBlock AMADRYS = add("amadrys", new AmadrysCropBlock(shrub().mapColor(MapColor.PINK)), flammablePlant, cutoutRenderLayer);

    public static final BlueberryBushBlock BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(of(Material.PLANT).strength(0.2f)
            .ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().suffocates(never).blockVision(never).noCollision()), flammablePlant, cutoutRenderLayer);
    // Flowers
    private static Settings flower() { return copy(DANDELION); }
    public static final FlowerBlock ANCIENT_FLOWER = add("ancient_flower", new FlowerBlock(StatusEffects.ABSORPTION, 20, flower()), flammablePlant, cutoutRenderLayer);
    //todo public static final FlowerPotBlock POTTED_ANCIENT_FLOWER = add("potted_ancient_flower", new FlowerPotBlock(ANCIENT_FLOWER, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock ATARAXIA = add("ataraxia", new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, flower()), flammablePlant, cutoutRenderLayer);
    //public static final FlowerPotBlock POTTED_ATARAXIA = add("potted_ataraxia", new FlowerPotBlock(ATARAXIA, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock CLOUDSBLUFF = add("cloudsbluff", new FlowerBlock(StatusEffects.SLOW_FALLING, 6, flower()), flammablePlant, cutoutRenderLayer);
    //public static final FlowerPotBlock POTTED_CLOUDSBLUFF = add("potted_cloudsbluff", new FlowerPotBlock(CLOUDSBLUFF, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock DRIGEAN = add("drigean", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, flower()), flammablePlant, cutoutRenderLayer);
    //public static final FlowerPotBlock POTTED_DRIGEAN = add("potted_drigean", new FlowerPotBlock(DRIGEAN, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock LUMINAR = add("luminar", new FlowerBlock(StatusEffects.GLOWING, 9, flower().luminance(value -> 3)), flammablePlant, cutoutRenderLayer);
    //public static final FlowerPotBlock POTTED_LUMINAR = add("potted_luminar", new FlowerPotBlock(LUMINAR, flowerPot().luminance(value -> 3)), cutoutRenderLayer);
    // Ores
    public static final OreBlock AMBROSIUM_ORE = add("ambrosium_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final OreBlock ZANITE_ORE = add("zanite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final FloatingBlock GRAVITITE_ORE = add("gravitite_ore", new FloatingBlock(false, of(Material.STONE).requiresTool().strength(5f).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)));
    public static final Block ZANITE_BLOCK = add("zanite_block", new Block(of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final FloatingBlock BLOCK_OF_GRAVITITE = add("block_of_gravitite", new FloatingBlock(false, of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    // Misc
    public static final FloatingBlock GRAVITITE_LEVITATOR = add("gravitite_levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));
    public static final ChainBlock ZANITE_CHAIN = add("zanite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final AmbrosiumLanternBlock AMBROSIUM_LANTERN = add("ambrosium_lantern", new AmbrosiumLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);
    public static final AetherPortalBlock BLUE_PORTAL = add("blue_portal", new AetherPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucentRenderLayer);
    // Torches
    private static Settings ambrosiumTorch() { return copy(TORCH).ticksRandomly().luminance(state -> 15); }
    public static final AmbrosiumTorchBlock AMBROSIUM_TORCH = add("ambrosium_torch", new AmbrosiumTorchBlock(ambrosiumTorch()), cutoutRenderLayer);
    public static final AmbrosiumWallTorchBlock AMBROSIUM_TORCH_WALL = add("ambrosium_wall_torch", new AmbrosiumWallTorchBlock(ambrosiumTorch().dropsLike(AMBROSIUM_TORCH)), cutoutRenderLayer);
    // Swet Drops
    private static Settings swetDrop() { return of(Material.SOLID_ORGANIC, MapColor.CLEAR).breakInstantly().noCollision(); }
    public static final SwetDropBlock SWET_DROP = add("swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.WHITE_SWET));
    public static final SwetDropBlock BLUE_SWET_DROP = add("blue_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.BLUE_SWET));
    public static final SwetDropBlock GOLDEN_SWET_DROP = add("golden_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.GOLDEN_SWET));
    public static final SwetDropBlock PURPLE_SWET_DROP = add("purple_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.PURPLE_SWET));
    // Usables
    public static final IncubatorBlock INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);


    @SafeVarargs
    private static <V extends Block> V add(String id, V block, Action<? super V>... additionalActions) {
        return AetherRegistryQueues.BLOCK.add(locate(id), block, additionalActions);
    }

    public static void init() {
        AetherRegistryQueues.BLOCK.register();
    }

    /**
     * Takes tree colours, and produces various block settings for that tree to ease the creation of wooden block sets
     */
    private static record WoodTypeFactory(MapColor woodColor, MapColor barkColor, MapColor leafColor, MapColor plankColor) {
        public WoodTypeFactory(MapColor woodColor, MapColor barkColor, MapColor leafColor) {
            this(woodColor, barkColor, leafColor, woodColor);
        }

        public WoodTypeFactory(MapColor woodColor, MapColor barkColor) {
            this(woodColor, barkColor, MapColor.DARK_GREEN, woodColor);
        }

        public WoodTypeFactory withLeafColor(MapColor color) {
            return new WoodTypeFactory(this.woodColor, this.barkColor, color, this.plankColor);
        }

        public Settings log() {
            Settings log = copy(OAK_LOG);
            ((AbstractBlockSettingsAccessor) log).setMapColorProvider(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? this.woodColor : this.barkColor);
            return log;
        }

        public Settings wood() {
            return copy(OAK_WOOD).mapColor(this.barkColor);
        }

        public Settings strippedLog() {
            return copy(STRIPPED_OAK_LOG).mapColor(this.woodColor);
        }

        public Settings strippedWood() {
            return copy(STRIPPED_OAK_WOOD).mapColor(this.woodColor);
        }

        public Settings sapling() {
            return copy(OAK_SAPLING).mapColor(this.leafColor);
        }

        public Settings leaves() {
            return copy(OAK_LEAVES).mapColor(this.leafColor);
        }

        public Settings wisteriaLeaves() {
            return this.leaves().noCollision().allowsSpawning((state, world, pos, type) -> false);
        }

        public Settings auralWisteriaLeaves() {
            return aural(this.wisteriaLeaves());
        }

        public Settings hanger() {
            return of(Material.DECORATION).strength(0.2f).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).suffocates(never).blockVision(never);
        }

        public Settings auralHanger() {
            return aural(this.hanger());
        }

        public Settings leafPile() {
            return of(Material.REPLACEABLE_PLANT, this.leafColor).strength(0.2f).sounds(BlockSoundGroup.VINE).nonOpaque().suffocates(never).blockVision(never);
        }

        public Settings planks() {
            return copy(OAK_PLANKS).mapColor(this.plankColor);
        }

        public Settings trapdoor() {
            return copy(OAK_TRAPDOOR).mapColor(this.plankColor);
        }

        public Settings door() {
            return copy(OAK_DOOR).mapColor(this.plankColor);
        }

        public Settings button() {
            return copy(OAK_BUTTON);
        }

        public Settings pressurePlate() {
            return copy(OAK_BUTTON).mapColor(this.plankColor);
        }

        public Settings sign() {
            return copy(OAK_SIGN).mapColor(this.plankColor);
        }

        public Settings wallSign() {
            return copy(OAK_WALL_SIGN).mapColor(this.plankColor);
        }

        private static Settings aural(Settings settings) {
            return settings.strength(0.05f).luminance(state -> 7).emissiveLighting(always).postProcess(always);
        }
    }

    private static class AetherWoodenButtonBlock extends WoodenButtonBlock {
        protected AetherWoodenButtonBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherDoorBlock extends DoorBlock {
        public AetherDoorBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherFarmlandBlock extends FarmlandBlock {
        public AetherFarmlandBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherPaneBlock extends PaneBlock {
        public AetherPaneBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherPressurePlateBlock extends PressurePlateBlock {
        public AetherPressurePlateBlock(ActivationRule type, Settings settings) {
            super(type, settings);
        }
    }

    private static class AetherStairsBlock extends StairsBlock {
        public AetherStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }

    private static class AetherTrapdoorBlock extends TrapdoorBlock {
        public AetherTrapdoorBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherWeightedPressurePlateBlock extends WeightedPressurePlateBlock {
        public AetherWeightedPressurePlateBlock(int weight, Settings settings) {
            super(weight, settings);
        }
    }
}
