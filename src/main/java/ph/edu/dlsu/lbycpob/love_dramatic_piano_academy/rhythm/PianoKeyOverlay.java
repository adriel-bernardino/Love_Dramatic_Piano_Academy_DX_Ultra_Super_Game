package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class PianoKeyOverlay {
    private KeyCode keyPressed;

    private Rectangle aOverlay;
    private Rectangle sOverlay;
    private Rectangle dOverlay;
    private Rectangle fOverlay;
    private Rectangle gOverlay;
    private Rectangle hOverlay;
    private Rectangle jOverlay;
    private Rectangle kOverlay;
    private Rectangle lOverlay;
    private Rectangle colOverlay;

    private Rectangle wOverlay;
    private Rectangle eOverlay;
    private Rectangle tOverlay;
    private Rectangle yOverlay;
    private Rectangle uOverlay;
    private Rectangle oOverlay;
    private Rectangle pOverlay;

    private final List<UserAction> actions = new ArrayList<>();
    private boolean active = false;

    public PianoKeyOverlay() {
        int appHeight = FXGL.getAppHeight();
        int appWidth = FXGL.getAppWidth();

        //White Piece
        aOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        aOverlay.setFill(Color.web("#2c2c2c"));
        aOverlay.setOpacity(0);
        aOverlay.setX(appWidth * 0.516927083);
        aOverlay.setY(appHeight * 0.725);

        sOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        sOverlay.setFill(Color.web("#2c2c2c"));
        sOverlay.setOpacity(0);
        sOverlay.setX(appWidth * 0.553967083);
        sOverlay.setY(appHeight * 0.725);

        dOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        dOverlay.setFill(Color.web("#2c2c2c"));
        dOverlay.setOpacity(0);
        dOverlay.setX(appWidth * 0.591007083);
        dOverlay.setY(appHeight * 0.725);

        fOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        fOverlay.setFill(Color.web("#2c2c2c"));
        fOverlay.setOpacity(0);
        fOverlay.setX(appWidth * 0.628047083);
        fOverlay.setY(appHeight * 0.725);

        gOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        gOverlay.setFill(Color.web("#2c2c2c"));
        gOverlay.setOpacity(0);
        gOverlay.setX(appWidth * 0.665087083);
        gOverlay.setY(appHeight * 0.725);

        hOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        hOverlay.setFill(Color.web("#2c2c2c"));
        hOverlay.setOpacity(0);
        hOverlay.setX(appWidth * 0.702127083);
        hOverlay.setY(appHeight * 0.725);

        jOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        jOverlay.setFill(Color.web("#2c2c2c"));
        jOverlay.setOpacity(0);
        jOverlay.setX(appWidth * 0.739167083);
        jOverlay.setY(appHeight * 0.725);

        kOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        kOverlay.setFill(Color.web("#2c2c2c"));
        kOverlay.setOpacity(0);
        kOverlay.setX(appWidth * 0.776207083);
        kOverlay.setY(appHeight * 0.725);

        lOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        lOverlay.setFill(Color.web("#2c2c2c"));
        lOverlay.setOpacity(0);
        lOverlay.setX(appWidth * 0.813247083);
        lOverlay.setY(appHeight * 0.725);

        colOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        colOverlay.setFill(Color.web("#2c2c2c"));
        colOverlay.setOpacity(0);
        colOverlay.setX(appWidth * 0.850287083);
        colOverlay.setY(appHeight * 0.725);

        FXGL.addUINode(colOverlay);
        FXGL.addUINode(lOverlay);
        FXGL.addUINode(kOverlay);
        FXGL.addUINode(jOverlay);
        FXGL.addUINode(hOverlay);
        FXGL.addUINode(gOverlay);
        FXGL.addUINode(fOverlay);
        FXGL.addUINode(dOverlay);
        FXGL.addUINode(aOverlay);
        FXGL.addUINode(sOverlay);



        //Black Piece
        wOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        wOverlay.setFill(Color.DARKGRAY);
        wOverlay.setOpacity(0);
        wOverlay.setX(appWidth * 0.54296875);
        wOverlay.setY(appHeight * 0.725);

        eOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        eOverlay.setFill(Color.DARKGRAY);
        eOverlay.setOpacity(0);
        eOverlay.setX(appWidth * 0.58);
        eOverlay.setY(appHeight * 0.725);

        tOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        tOverlay.setFill(Color.DARKGRAY);
        tOverlay.setOpacity(0);
        tOverlay.setX(appWidth * 0.654);
        tOverlay.setY(appHeight * 0.725);

        yOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        yOverlay.setFill(Color.DARKGRAY);
        yOverlay.setOpacity(0);
        yOverlay.setX(appWidth * 0.69104);
        yOverlay.setY(appHeight * 0.725);

        uOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        uOverlay.setFill(Color.DARKGRAY);
        uOverlay.setOpacity(0);
        uOverlay.setX(appWidth * 0.72808);
        uOverlay.setY(appHeight * 0.725);

        oOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        oOverlay.setFill(Color.DARKGRAY);
        oOverlay.setOpacity(0);
        oOverlay.setX(appWidth * 0.80208);
        oOverlay.setY(appHeight * 0.725);

        pOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        pOverlay.setFill(Color.DARKGRAY);
        pOverlay.setOpacity(0);
        pOverlay.setX(appWidth * 0.83912);
        pOverlay.setY(appHeight * 0.725);

        FXGL.addUINode(pOverlay);
        FXGL.addUINode(oOverlay);
        FXGL.addUINode(uOverlay);
        FXGL.addUINode(yOverlay);
        FXGL.addUINode(tOverlay);
        FXGL.addUINode(eOverlay);
        FXGL.addUINode(wOverlay);

        UserAction aAction= new UserAction("A Highlight") {
            @Override
            protected void onActionBegin() {
                press(aOverlay,KeyCode.A);
            }
            @Override
            protected void onActionEnd() {
                aOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(aAction, KeyCode.A);
        actions.add(aAction);

        UserAction SAction= new UserAction("S Highlight") {
            @Override
            protected void onActionBegin() {
                press(sOverlay, KeyCode.S);
            }
            @Override
            protected void onActionEnd() {
                sOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(SAction, KeyCode.S);
        actions.add(SAction);

        UserAction dAction= new UserAction("D Highlight") {
            @Override
            protected void onActionBegin() {
                press(dOverlay, KeyCode.D);
            }
            @Override
            protected void onActionEnd() {
                dOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(dAction, KeyCode.D);
        actions.add(dAction);


        UserAction FAction= new UserAction("F Highlight") {
            @Override
            protected void onActionBegin() {
                press(fOverlay, KeyCode.F);
            }
            @Override
            protected void onActionEnd() {
                fOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(FAction, KeyCode.F);
        actions.add(FAction);


        UserAction gaction= new UserAction("G Highlight") {
            @Override
            protected void onActionBegin() {
                press(gOverlay, KeyCode.G);
            }
            @Override
            protected void onActionEnd() {
                gOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(gaction, KeyCode.G);
        actions.add(gaction);

        UserAction HAction= new UserAction("H Highlight") {
            @Override
            protected void onActionBegin() {
                press(hOverlay, KeyCode.H);
            }
            @Override
            protected void onActionEnd() {
                hOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(HAction, KeyCode.H);
        actions.add(HAction);

        UserAction JAction= new UserAction("J Highlight") {
            @Override
            protected void onActionBegin() {
                press(jOverlay, KeyCode.J);
            }
            @Override
            protected void onActionEnd() {
                jOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(JAction, KeyCode.J);
        actions.add(JAction);

        UserAction KAction= new UserAction("K Highlight") {
            @Override
            protected void onActionBegin() {
                press(kOverlay, KeyCode.K);
            }
            @Override
            protected void onActionEnd() {
                kOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(KAction, KeyCode.K);
        actions.add(KAction);

        UserAction LAction= new UserAction("L Highlight") {
            @Override
            protected void onActionBegin() {
                press(lOverlay, KeyCode.L);
            }
            @Override
            protected void onActionEnd() {
                lOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(LAction, KeyCode.L);
        actions.add(LAction);

        UserAction SemiAction= new UserAction("Semicol Highlight") {
            @Override
            protected void onActionBegin() {
                press(colOverlay, KeyCode.SEMICOLON);
            }
            @Override
            protected void onActionEnd() {
                colOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(SemiAction, KeyCode.SEMICOLON);
        actions.add(SemiAction);


        UserAction WAction= new UserAction("W Highlight") {
            @Override
            protected void onActionBegin() {
                press(wOverlay, KeyCode.W);
            }
            @Override
            protected void onActionEnd() {
                wOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(WAction, KeyCode.W);
        actions.add(WAction);
        UserAction EAction= new UserAction("E Highlight") {
            @Override
            protected void onActionBegin() {
                press(eOverlay, KeyCode.E);
            }
            @Override
            protected void onActionEnd() {
                eOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(EAction, KeyCode.E);
        actions.add(EAction);
        UserAction TAction= new UserAction("T Highlight") {
            @Override
            protected void onActionBegin() {
                press(tOverlay, KeyCode.T);
            }
            @Override
            protected void onActionEnd() {
                tOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(TAction, KeyCode.T);
        actions.add(TAction);

        UserAction YAction= new UserAction("Y Highlight") {
            @Override
            protected void onActionBegin() {
                press(yOverlay, KeyCode.Y);
            }
            @Override
            protected void onActionEnd() {
                yOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(YAction, KeyCode.Y);
        actions.add(YAction);

        UserAction UAction= new UserAction("U Highlight") {
            @Override
            protected void onActionBegin() {
                press(uOverlay, KeyCode.U);
            }
            @Override
            protected void onActionEnd() {
                uOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(UAction, KeyCode.U);
        actions.add(UAction);

        UserAction OAction= new UserAction("O Highlight") {
            @Override
            protected void onActionBegin() {
                press(oOverlay, KeyCode.O);
            }
            @Override
            protected void onActionEnd() {
                oOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(OAction, KeyCode.O);
        actions.add(OAction);

        UserAction PAction= new UserAction("P Highlight") {
            @Override
            protected void onActionBegin() {
                press(pOverlay, KeyCode.P);
            }
            @Override
            protected void onActionEnd() {
                pOverlay.setOpacity(0);
            }
        };
        FXGL.getInput().addAction(PAction, KeyCode.P);
        actions.add(PAction);
    }

    private void press(Rectangle overlay, KeyCode key) {
        if (!active) return;
        overlay.setOpacity(1);
        keyPressStorage(key);
    }

    public void keyPressStorage(KeyCode key){
        if (!active) {
            return;
        }

        keyPressed = key;
    };
    public KeyCode consumeKeyPress() {
        KeyCode key = keyPressed;
        keyPressed = null;
        return key;
    }
    public KeyCode getKeyPressed() {
        return keyPressed;
    }

    public void activate() {
        active = true;
    }

    public void hide() {
        active = false;
        keyPressed = null;
        aOverlay.setOpacity(0);
        sOverlay.setOpacity(0);
        dOverlay.setOpacity(0);
        fOverlay.setOpacity(0);
        gOverlay.setOpacity(0);
        hOverlay.setOpacity(0);
        jOverlay.setOpacity(0);
        kOverlay.setOpacity(0);
        lOverlay.setOpacity(0);
        colOverlay.setOpacity(0);

        wOverlay.setOpacity(0);
        eOverlay.setOpacity(0);
        tOverlay.setOpacity(0);
        yOverlay.setOpacity(0);
        uOverlay.setOpacity(0);
        oOverlay.setOpacity(0);
        pOverlay.setOpacity(0);
    }
}
