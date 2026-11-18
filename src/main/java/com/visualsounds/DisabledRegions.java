package com.visualsounds;

import java.util.Set;

/**
 * Areas where the plugin must stay disabled to comply with the
 * <a href="https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1">third-party client guidelines</a>.
 * <p>
 * To disable the plugin in a new area, add an entry to this enum. Use https://explv.github.io to find region ids.
 * Region ids are the ones returned by {@code WorldPoint.fromLocalInstance(...).getRegionID()}, so they refer to
 * the template region when the player is inside an instance.
 */
public enum DisabledRegions
{
	ALCHEMICAL_HYDRA(Set.of(5536)),
	VARDORVIS(Set.of(4405)),
	LEVIATHAN(Set.of(8291)),
	WHISPERER(Set.of(10595)),
	SUCELLUS(Set.of(12132)),
	VORKATH(Set.of(9023)),
	INFERNO(Set.of(9043)),
	FIGHT_CAVE(Set.of(9551)),
	GAUNTLET(Set.of(7512, 7768)), // Corrupted Gauntlet, Gauntlet
	COLOSSEUM(Set.of(7216)),
	KALPHITE_QUEEN(Set.of(13972), Set.of(0)),
	COX(Set.of(
		13136, // End of floor
		13137, 13393, // Lobbies/Room transitions
		13138, 13394, // Vasa/Tekton/Vespula Lizardmen/Skeletal Mystics/Guardian
		13139, 13395, 13140, 13396, // Puzzle rooms/bosses
		13141, 13397, // Rest room
		13145, // New floor
		13401, // New floor?
		12889 // Olm
	)),
	TOB(Set.of(
		12613, // Maiden
		13125, // Bloat
		13122, // Nylocas
		13123, 13379, // Sotetseg/maze
		12612, // Xarpus
		12611 // Verzik
	)),
	TOA(Set.of(
		14160, // Nexus lobby
		15698, // Crondis
		15700, // Zebak
		14162, // Scabaras
		14164, // Kephri
		15186, // Apmeken
		15188, // Ba-Ba
		14674, // Het
		14676, // Akkha
		15184, 15696 // Wardens
	)),
	YAMA(Set.of(6045)),
	DOOM_OF_MOKHAIOTL(Set.of(5269, 13668, 14180)),
	NIGHTMARE(Set.of(15515)),
	MUSPAH(Set.of(11330)),
	MAGGOT_KING(Set.of(11645, 11901)),
	TFA(Set.of(16214, 16726, 16215, 16727, 16216, 16728, 16472, 16471));

	private final Set<Integer> regionIds;
	/**
	 * Planes the region is disabled on. Empty means every plane.
	 */
	private final Set<Integer> planes;

	DisabledRegions(Set<Integer> regionIds)
	{
		this(regionIds, Set.of());
	}

	DisabledRegions(Set<Integer> regionIds, Set<Integer> planes)
	{
		this.regionIds = regionIds;
		this.planes = planes;
	}

	private boolean matches(int regionId, int plane)
	{
		return regionIds.contains(regionId)
			&& (planes.isEmpty() || planes.contains(plane));
	}

	/**
	 * @param regionId   the region the player is currently in
	 * @param plane      the plane the player is currently on
	 * @return whether the plugin must be disabled in the given location
	 */
	public static boolean isDisabled(int regionId, int plane)
	{
		for (DisabledRegions region : values())
		{
			if (region.matches(regionId, plane))
			{
				return true;
			}
		}
		return false;
	}
}
