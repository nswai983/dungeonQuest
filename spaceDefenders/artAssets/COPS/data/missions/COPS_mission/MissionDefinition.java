package data.missions.COPS_mission;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "COP", FleetGoal.ATTACK, false);	// Fleet goal can also be "FleetGoal.ESCAPE" for pursuit battles.
		api.initFleet(FleetSide.ENEMY, "TSS", FleetGoal.ATTACK, true);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Your fleet");
		api.setFleetTagline(FleetSide.ENEMY, "Enemy fleet");

		// These show up as items in the bulleted list under
		// "Tactical Objectives" on the mission detail screen
		//api.addBriefingItem("Briefing Item 1");
		//api.addBriefingItem("Briefing Item 2");
		//api.addBriefingItem("Briefing Item 3");

		// Set up the player's fleet.  Variant names come from the
		// files in data/variants
		api.addToFleet(FleetSide.PLAYER, "COPS_asimov_elite", FleetMemberType.SHIP, "Asimov", true);
		api.addToFleet(FleetSide.PLAYER, "COPS_mesonax_assault", FleetMemberType.SHIP, "Mesonax", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_atome_standard", FleetMemberType.SHIP, "Atome", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_atome_BO_assault", FleetMemberType.SHIP, "Atome (BO)", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_antyma_assault", FleetMemberType.SHIP, "Antyma", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_antyma_pirate_balanced", FleetMemberType.SHIP, "Antyma MK.II", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_gravon_beam", FleetMemberType.SHIP, "Gravon", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_doom_support", FleetMemberType.SHIP, "Gravon", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_rhodan_standard", FleetMemberType.SHIP, "Rhodan", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_electras_standard", FleetMemberType.SHIP, "Electras", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_haydron_support", FleetMemberType.SHIP, "Haydron", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_haydron_BO_attack", FleetMemberType.SHIP, "Haydron", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_lepton_attack", FleetMemberType.SHIP, "Lepton", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_neutroyte_laser", FleetMemberType.SHIP, "Neutroyte", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_neutroyte_pirate_balanced", FleetMemberType.SHIP, "Neutroyte MK.II", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_proteus_standard", FleetMemberType.SHIP, "Photoal", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_bosana_assault", FleetMemberType.SHIP, "Bosana", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_bosana_s_assault", FleetMemberType.SHIP, "Bosana (Stock)", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_chrone_assault", FleetMemberType.SHIP, "Chrone", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_photoal_laser", FleetMemberType.SHIP, "Photoal", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_gluoni_assault", FleetMemberType.SHIP, "Gluoni", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_tau_elite", FleetMemberType.SHIP, "Tau", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_quantum_strike", FleetMemberType.SHIP, "Quantum", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_quantum_BO_elite", FleetMemberType.SHIP, "Quantum (BO)", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_tempest_strike", FleetMemberType.SHIP, "Tempest (COP)", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_carbon_attack", FleetMemberType.SHIP, "Carbon", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_valence_balanced", FleetMemberType.SHIP, "Valence", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_wolf_cop_balanced", FleetMemberType.SHIP, "Wolf (COP)", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_muonay_balanced", FleetMemberType.SHIP, "Muonay", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_planck_escort", FleetMemberType.SHIP, "Planck", false);
		api.addToFleet(FleetSide.PLAYER, "COPS_schala_strike", FleetMemberType.SHIP, "Schala", false);
		//api.addToFleet(FleetSide.PLAYER, "SHIP_VARIANT_ID", FleetMemberType.SHIP, "Another Ship Name", false);	// A friendly ship (named)
		//api.addToFleet(FleetSide.PLAYER, "SHIP_VARIANT_ID", FleetMemberType.SHIP, false);						// Another friendly ship (not named)

		// Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "dram_Light", FleetMemberType.SHIP, "Poor Sod", false);		// Enemy ship
		//api.addToFleet(FleetSide.ENEMY, "SHIP_VARIANT_ID", FleetMemberType.SHIP, "Bad Ship Name", false);		// Another enemy ship

		//api.defeatOnShipLoss("A Ship Name");	// The mission ends if the ship with this name dies. Can be friendly or enemy.
		//api.defeatOnShipLoss("Evil Ship Name");

		// Set up the map.
		float width = 12000f;	// 1000 units = about one grid cell on the tactical map
		float height = 12000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);

		float minX = -width/2;
		float minY = -height/2;

		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);

		api.addPlanet(0, 0, 50f, StarTypes.RED_GIANT, 250f, true);

	}

}
