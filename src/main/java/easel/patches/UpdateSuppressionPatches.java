package easel.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import easel.utils.UpdateSuppressor;

public class UpdateSuppressionPatches {
    @SpirePatch( clz = TopPanel.class, method = "update" )
    public static class TopPanelSuppressInputPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(TopPanel _instance) {
            //if (ScreenHelper.isScreenUp())
            if (UpdateSuppressor.isSuppressingUpdates())
                return SpireReturn.Return(null);
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch( clz = TipHelper.class, method = "render" )
    public static class TipSuppressorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SpriteBatch _sb) {
//            if (ScreenHelper.isScreenUp())
            if (UpdateSuppressor.isSuppressingTips())
                return SpireReturn.Return(null);
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch( clz = AbstractDungeon.class, method = "update" )
    public static class AbstractDungeonUpdateSuppressPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractDungeon _instance) {
//            ScreenHelper.update();

//            if (ScreenHelper.isScreenUp())
            if (UpdateSuppressor.isSuppressingUpdates())
                return SpireReturn.Return(null);
            else
                return SpireReturn.Continue();
        }
    }
}
