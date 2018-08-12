package exnihilocreatio.registries.manager

import exnihilocreatio.registries.registries.*
import exnihilocreatio.registries.registries.prefab.BaseRegistry

interface IDefaultRecipeProvider<in T : BaseRegistry<*>> {
    fun registerRecipeDefaults(registry: T)
}

interface IFluidBlockDefaultRegistryProvider : IDefaultRecipeProvider<FluidBlockTransformerRegistry>
interface IFluidItemFluidDefaultRegistryProvider : IDefaultRecipeProvider<FluidItemFluidRegistry>
interface IFluidOnTopDefaultRegistryProvider : IDefaultRecipeProvider<FluidOnTopRegistry>
interface IFluidTransformDefaultRegistryProvider : IDefaultRecipeProvider<FluidTransformRegistry>
interface IHammerDefaultRegistryProvider : IDefaultRecipeProvider<HammerRegistry>
interface IHeatDefaultRegistryProvider : IDefaultRecipeProvider<HeatRegistry>
interface IMilkEntityDefaultRegistryProvider : IDefaultRecipeProvider<MilkEntityRegistry>
interface IOreDefaultRegistryProvider : IDefaultRecipeProvider<OreRegistry>
interface ISieveDefaultRegistryProvider : IDefaultRecipeProvider<SieveRegistry>
interface ICrucibleWoodDefaultRegistryProvider : IDefaultRecipeProvider<CrucibleRegistry>
interface ICrucibleStoneDefaultRegistryProvider : IDefaultRecipeProvider<CrucibleRegistry>
interface ICrookDefaultRegistryProvider : IDefaultRecipeProvider<CrookRegistry>
interface ICompostDefaultRegistryProvider : IDefaultRecipeProvider<CompostRegistry>
interface IBarrelLiquidBlacklistDefaultRegistryProvider : IDefaultRecipeProvider<BarrelLiquidBlacklistRegistry>