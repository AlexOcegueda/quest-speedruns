package com.questhelper.helpers.speedruns.xmarksthespot;

import com.questhelper.collections.ItemCollections;
import com.questhelper.rewards.ItemReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DigStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.QuestStep;
import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.requirements.util.Operation;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMarksTheSpotSpeedrun extends BasicQuestHelper
{
    ItemRequirement spade;
    Requirement hasSpade;

    QuestStep speakVeosLumbridge, buySpade, digOutsideBob, digCastle, digDraynor, digMartin, speakVeosSarim, speakVeosSarimWithoutCasket;
    ConditionalStep buySpadeConditional;

    @Override
    public Map<Integer, QuestStep> loadSteps()
    {
        setupRequirements();
        setupConditions();
        setupSteps();

        Map<Integer, QuestStep> steps = new HashMap<>();

        steps.put(0, speakVeosLumbridge);
        steps.put(1, speakVeosLumbridge);
        steps.put(2, buySpadeConditional);
        steps.put(3, digOutsideBob);
        steps.put(4, digCastle);
        steps.put(5, digDraynor);
        steps.put(6, digMartin);
        steps.put(7, speakVeosSarim);
        steps.put(8, speakVeosSarimWithoutCasket);

        return steps;
    }

    @Override
    public void setupRequirements()
    {
        spade = new ItemRequirement("Spade", ItemID.SPADE).isNotConsumed();
    }

    private void setupSteps()
    {
        speakVeosLumbridge = new NpcStep(this, NpcID.VEOS_8484, new WorldPoint(3228, 3242, 0),
            "Talk to Veos in The Sheared Ram pub in Lumbridge to start the quest.");
        speakVeosLumbridge.addDialogStep("I'm looking for a quest.");
        speakVeosLumbridge.addDialogStep("Sounds good, what should I do?");
        speakVeosLumbridge.addDialogSteps("Can I help?", "Yes.");

        buySpade = new NpcStep(this, 2813, new WorldPoint(3210, 3216, 0), "Trade Shop Keeper to buy a spade.");

        digOutsideBob = new DigStep(this, new WorldPoint(3230, 3209, 0),
            "Dig north of Bob's Brilliant Axes, on the west side of the plant against the wall of his house.", spade);
        digOutsideBob.addDialogStep("Okay, thanks Veos.");

        digCastle = new DigStep(this, new WorldPoint(3203, 3212, 0),
            "Dig behind Lumbridge Castle, just outside the kitchen door.", spade);

        digDraynor = new DigStep(this, new WorldPoint(3109, 3264, 0),
            "Dig north-west of the Draynor Village jail, just by the wheat farm.", spade);

        digMartin = new DigStep(this, new WorldPoint(3078, 3259, 0),
            "Dig in the pig pen just west where Martin the Master Gardener is.", spade);

        ItemRequirement ancientCasket = new ItemRequirement("Ancient casket", ItemID.ANCIENT_CASKET);
        ancientCasket.setTooltip("If you've lost this you can get another by digging in the pig pen in Draynor Village.");

        speakVeosSarim = new NpcStep(this, NpcID.VEOS_8484, new WorldPoint(3054, 3245, 0),
            "Talk to Veos directly south of the Rusty Anchor Inn in Port Sarim to finish the quest.", ancientCasket);
        ((NpcStep) speakVeosSarim).addAlternateNpcs(NpcID.VEOS_8630);

        speakVeosSarimWithoutCasket = new NpcStep(this, NpcID.VEOS_8484, new WorldPoint(3054, 3245, 0),
            "Talk to Veos directly south of the Rusty Anchor Inn in Port Sarim to finish the quest.");
        ((NpcStep) speakVeosSarimWithoutCasket).addAlternateNpcs(NpcID.VEOS_8630);

        speakVeosSarim.addSubSteps(speakVeosSarimWithoutCasket);

        // Create a conditional step that checks if the player has the spade
        buySpadeConditional = new ConditionalStep(this, buySpade, hasSpade);
    }

    public void setupConditions() 
    {
        hasSpade = new VarbitRequirement(8063, 1);
    }

    @Override
    public List<ItemRequirement> getItemRequirements()
    {
        ArrayList<ItemRequirement> reqs = new ArrayList<>();
        reqs.add(spade);
        return reqs;
    }

    @Override
    public List<ItemRequirement> getItemRecommended()
    {
        ArrayList<ItemRequirement> reqs = new ArrayList<>();
        reqs.add(new ItemRequirement("Amulet of Glory for faster teleport to Draynor Village.", ItemCollections.AMULET_OF_GLORIES).isNotConsumed());
        return reqs;
    }

    @Override
    public QuestPointReward getQuestPointReward()
    {
        return new QuestPointReward(1);
    }

    @Override
    public List<ItemReward> getItemRewards()
    {
        return Arrays.asList(
            new ItemReward("300 Exp. Lamp (Any Skill)", ItemID.ANTIQUE_LAMP, 1),
            new ItemReward("Coins", ItemID.COINS_995, 200),
            new ItemReward("A Beginner Clue Scroll", ItemID.CLUE_SCROLL_BEGINNER, 1)
        );
    }

    @Override
    public List<PanelDetails> getPanels()
    {
        List<PanelDetails> allSteps = new ArrayList<>();
        allSteps.add(new PanelDetails("Speak to Veos", Collections.singletonList(speakVeosLumbridge), spade));
        allSteps.add(new PanelDetails("Buy a Spade from the General Store", Collections.singletonList(buySpade), spade));
        allSteps.add(new PanelDetails("Solve the Clue Scroll", Arrays.asList(digOutsideBob, digCastle, digDraynor, digMartin)));
        allSteps.add(new PanelDetails("Bring the Casket to Veos", Collections.singletonList(speakVeosSarim)));
        return allSteps;
    }
}
