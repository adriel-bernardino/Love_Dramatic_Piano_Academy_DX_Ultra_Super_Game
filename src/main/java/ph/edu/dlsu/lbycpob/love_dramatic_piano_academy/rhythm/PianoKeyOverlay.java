package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PianoKeyOverlay {
    PianoKeyOverlay() {
        int appHeight = FXGL.getAppHeight();
        int appWidth = FXGL.getAppWidth();

        //White Piece
        Rectangle aOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        aOverlay.setFill(Color.web("#2c2c2c"));
        aOverlay.setOpacity(0);
        aOverlay.setX(appWidth * 0.516927083);
        aOverlay.setY(appHeight * 0.725);

        Rectangle sOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        sOverlay.setFill(Color.web("#2c2c2c"));
        sOverlay.setOpacity(0);
        sOverlay.setX(appWidth * 0.553967083);
        sOverlay.setY(appHeight * 0.725);

        Rectangle dOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        dOverlay.setFill(Color.web("#2c2c2c"));
        dOverlay.setOpacity(0);
        dOverlay.setX(appWidth * 0.591007083);
        dOverlay.setY(appHeight * 0.725);

        Rectangle fOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        fOverlay.setFill(Color.web("#2c2c2c"));
        fOverlay.setOpacity(0);
        fOverlay.setX(appWidth * 0.628047083);
        fOverlay.setY(appHeight * 0.725);

        Rectangle gOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        gOverlay.setFill(Color.web("#2c2c2c"));
        gOverlay.setOpacity(0);
        gOverlay.setX(appWidth * 0.665087083);
        gOverlay.setY(appHeight * 0.725);

        Rectangle hOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        hOverlay.setFill(Color.web("#2c2c2c"));
        hOverlay.setOpacity(0);
        hOverlay.setX(appWidth * 0.702127083);
        hOverlay.setY(appHeight * 0.725);

        Rectangle jOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        jOverlay.setFill(Color.web("#2c2c2c"));
        jOverlay.setOpacity(0);
        jOverlay.setX(appWidth * 0.739167083);
        jOverlay.setY(appHeight * 0.725);

        Rectangle kOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        kOverlay.setFill(Color.web("#2c2c2c"));
        kOverlay.setOpacity(0);
        kOverlay.setX(appWidth * 0.776207083);
        kOverlay.setY(appHeight * 0.725);

        Rectangle lOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
        lOverlay.setFill(Color.web("#2c2c2c"));
        lOverlay.setOpacity(0);
        lOverlay.setX(appWidth * 0.813247083);
        lOverlay.setY(appHeight * 0.725);

        Rectangle colOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.13);
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
        Rectangle wOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        wOverlay.setFill(Color.DARKGRAY);
        wOverlay.setOpacity(0);
        wOverlay.setX(appWidth * 0.54296875);
        wOverlay.setY(appHeight * 0.725);

        Rectangle eOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        eOverlay.setFill(Color.DARKGRAY);
        eOverlay.setOpacity(0);
        eOverlay.setX(appWidth * 0.58);
        eOverlay.setY(appHeight * 0.725);

        Rectangle tOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        tOverlay.setFill(Color.DARKGRAY);
        tOverlay.setOpacity(0);
        tOverlay.setX(appWidth * 0.654);
        tOverlay.setY(appHeight * 0.725);

        Rectangle yOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        yOverlay.setFill(Color.DARKGRAY);
        yOverlay.setOpacity(0);
        yOverlay.setX(appWidth * 0.69104);
        yOverlay.setY(appHeight * 0.725);

        Rectangle uOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        uOverlay.setFill(Color.DARKGRAY);
        uOverlay.setOpacity(0);
        uOverlay.setX(appWidth * 0.72808);
        uOverlay.setY(appHeight * 0.725);

        Rectangle oOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
        oOverlay.setFill(Color.DARKGRAY);
        oOverlay.setOpacity(0);
        oOverlay.setX(appWidth * 0.80208);
        oOverlay.setY(appHeight * 0.725);

        Rectangle pOverlay = new Rectangle(appWidth * 0.035, appHeight * 0.0675);
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
        FXGL.getInput().addAction(
                new UserAction("A Highlight") {
                    @Override
                    protected void onActionBegin() {
                        aOverlay.setOpacity(1);
                    }

                    @Override
                    protected void onActionEnd() {
                        aOverlay.setOpacity(0);
                    }
                },
                KeyCode.A
        );
        FXGL.getInput().addAction(
                new UserAction("S Highlight") {
                    @Override
                    protected void onActionBegin() {
                        sOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        sOverlay.setOpacity(0);
                    }
                },
                KeyCode.S
        );
        FXGL.getInput().addAction(
                new UserAction("D Highlight") {
                    @Override
                    protected void onActionBegin() {
                        dOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        dOverlay.setOpacity(0);
                    }
                },
                KeyCode.D
        );
        FXGL.getInput().addAction(
                new UserAction("F Highlight") {
                    @Override
                    protected void onActionBegin() {
                        fOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        fOverlay.setOpacity(0);
                    }
                },
                KeyCode.F
        );
        FXGL.getInput().addAction(
                new UserAction("G Highlight") {
                    @Override
                    protected void onActionBegin() {
                        gOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        gOverlay.setOpacity(0);
                    }
                },
                KeyCode.G
        );
        FXGL.getInput().addAction(
                new UserAction("H Highlight") {
                    @Override
                    protected void onActionBegin() {
                        hOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        hOverlay.setOpacity(0);
                    }
                },
                KeyCode.H
        );
        FXGL.getInput().addAction(
                new UserAction("J Highlight") {
                    @Override
                    protected void onActionBegin() {
                        jOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        jOverlay.setOpacity(0);
                    }
                },
                KeyCode.J
        );
        FXGL.getInput().addAction(
                new UserAction("J Highlight") {
                    @Override
                    protected void onActionBegin() {
                        jOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        jOverlay.setOpacity(0);
                    }
                },
                KeyCode.J
        );
        FXGL.getInput().addAction(
                new UserAction("K Highlight") {
                    @Override
                    protected void onActionBegin() {
                        kOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        kOverlay.setOpacity(0);
                    }
                },
                KeyCode.K
        );
        FXGL.getInput().addAction(
                new UserAction("L Highlight") {
                    @Override
                    protected void onActionBegin() {
                        lOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        lOverlay.setOpacity(0);
                    }
                },
                KeyCode.L
        );
        FXGL.getInput().addAction(
                new UserAction("Semi-col Highlight") {
                    @Override
                    protected void onActionBegin() {
                        colOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        colOverlay.setOpacity(0);
                    }
                },
                KeyCode.SEMICOLON
        );
        FXGL.getInput().addAction(
                new UserAction("W Highlight") {
                    @Override
                    protected void onActionBegin() {
                        wOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        wOverlay.setOpacity(0);
                    }
                },
                KeyCode.W
        );
        FXGL.getInput().addAction(
                new UserAction("E Highlight") {
                    @Override
                    protected void onActionBegin() {
                        eOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        eOverlay.setOpacity(0);
                    }
                },
                KeyCode.E
        );
        FXGL.getInput().addAction(
                new UserAction("T Highlight") {
                    @Override
                    protected void onActionBegin() {
                        tOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        tOverlay.setOpacity(0);
                    }
                },
                KeyCode.T
        );
        FXGL.getInput().addAction(
                new UserAction("Y Highlight") {
                    @Override
                    protected void onActionBegin() {
                        yOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        yOverlay.setOpacity(0);
                    }
                },
                KeyCode.Y
        );
        FXGL.getInput().addAction(
                new UserAction("U Highlight") {
                    @Override
                    protected void onActionBegin() {
                        uOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        uOverlay.setOpacity(0);
                    }
                },
                KeyCode.U
        );
        FXGL.getInput().addAction(
                new UserAction("O Highlight") {
                    @Override
                    protected void onActionBegin() {
                        oOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        oOverlay.setOpacity(0);
                    }
                },
                KeyCode.O
        );
        FXGL.getInput().addAction(
                new UserAction("P Highlight") {
                    @Override
                    protected void onActionBegin() {
                        pOverlay.setOpacity(1);
                    }
                    @Override
                    protected void onActionEnd() {
                        pOverlay.setOpacity(0);
                    }
                },
                KeyCode.P
        );
    }
}
