# Patches and Beyond
A set of minor patches for various mods included in [Create: Above and Beyond](https://www.curseforge.com/minecraft/modpacks/create-above-and-beyond).

* Reverted the bounding box size of crystal seeds from AE2 to be consistent with regular items.
	* Crystal seeds normally have a taller bounding box which causes them to get stuck when dropped down from a chute or funnel from Create.
	* The increased height was originally a fix for an awkward looking interaction when crystal seeds are collected by an ME annihilation plane, undoing it reintroduces this issue but you are very unlikely to notice it while playing this modpack.
* Changed the default value of the waterlogged state of dynamos from Thermal Expansion from true to false.
	* The latest version of Thermal Foundation allows dynamos to be waterlogged but without this fix, loading an older save will cause all dynamos in the world to become waterlogged.
* Fixed singularities, charged certus quartz and crystal seeds not having some extra data properly set when produced by crushing wheels and other components from Create.
	* This mainly fixes singularities getting stuck when produced by a crushing wheel setup without a chute or a belt directly below.
* Fixed hose pulleys not consuming input fluids when receiving more than 1000 mB per tick.
	* This was a really easy way to duplicate any material in molten form and could be easily done by accident with a fluid cell from Thermal.
* Fixed bulk blasting setups destroying items if a stack of items results in more than one stack of outputs.
	* This fixes bulk blasting nickel compound ingots in-world causing all but one unprocessed invar ingot to disappear.
* Quantum-entangled singularities now have their velocity set to zero when produced by exploding a singularity.
	* This was originally handled by a KubeJS script in order to make automation easier but had the side effect of causing quantum-entangled singularities to immediately drop to the ground when thrown by a player or belt.
