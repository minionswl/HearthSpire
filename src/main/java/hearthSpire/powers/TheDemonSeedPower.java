package hearthSpire.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hearthSpire.DefaultMod;
import hearthSpire.util.TextureLoader;

import static hearthSpire.DefaultMod.makePowerPath;

public class TheDemonSeedPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID("TheDemonSeedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int counter = 0;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("TheDemonSeed_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("TheDemonSeed_power32.png"));

    public TheDemonSeedPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;


        type = PowerType.BUFF;
        isTurnBased = false;
        this.owner = owner;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }



    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner == this.owner) {
            counter += damageAmount;
            if (counter >= 8) {
                this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, 8, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new HealAction(owner, owner, 8));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new EstablishtheLinkPower(this.owner), 1));
            }
        }
    }



}