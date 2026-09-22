package org.firstinspires.ftc.teamcode.Components.Intake;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    ElapsedTime timer;
    public ActiveIntake activeIntake;
    public Transfer transfer;
    public Pitch pitch;
    public Storage storage;
    public FlowerRamp flowerRamp;
    public enum State{
        IDLE,
        REVERSE,
        INTAKE,
        INTAKE_FROM_FLOWER,
        REVERSE_FROM_FLOWER,
        CONTINUOUS_SHOOT,
        SHOOT(IDLE);
        State nextState;
        State(State nextState){
            this.nextState = nextState;
        }
        State(){
            this.nextState = this;
        }
    }
    public State state;
    public Intake(){
        timer = new ElapsedTime();
        state = State.IDLE;
        activeIntake = new ActiveIntake();
        transfer = new Transfer();
        pitch = new Pitch();
        storage = new Storage();
    }
    public void update(){
        stateUpdate();
        activeIntake.update();
        transfer.update();
        pitch.update();
        storage.update();
        flowerRamp.updateHardware();

        if (state != State.SHOOT) timer.reset();
    }
    public void stateUpdate(){
        switch (state){
            case IDLE:
                activeIntake.setState(ActiveIntake.State.IDLE);
                transfer.setState(Transfer.State.IDLE);
                pitch.setState(Pitch.State.IDLE);
                flowerRamp.setState(FlowerRamp.State.GOING_UP);
                break;
            case INTAKE:
                activeIntake.setState(ActiveIntake.State.INTAKE);
                transfer.setState(Transfer.State.ACTIVE);
                pitch.setState(Pitch.State.IDLE);
                flowerRamp.setState(FlowerRamp.State.GOING_UP);
                break;
            case INTAKE_FROM_FLOWER:
                activeIntake.setState(ActiveIntake.State.INTAKE);
                transfer.setState(Transfer.State.ACTIVE);
                pitch.setState(Pitch.State.IDLE);
                flowerRamp.setState(FlowerRamp.State.GOING_DOWN);
                break;
            case REVERSE_FROM_FLOWER:
                activeIntake.setState(ActiveIntake.State.REVERSE);
                transfer.setState(Transfer.State.ACTIVE);
                pitch.setState(Pitch.State.IDLE);
                flowerRamp.setState(FlowerRamp.State.GOING_DOWN);
                break;
            case REVERSE:
                activeIntake.setState(ActiveIntake.State.REVERSE);
                transfer.setState(Transfer.State.IDLE);
                pitch.setState(Pitch.State.IDLE);
                flowerRamp.setState(FlowerRamp.State.GOING_UP);
                break;
            case SHOOT:
                if (timer.seconds()>0.5 || storage.getState() == Storage.State.NO_POLLEN) state = state.nextState;
                if (pitch.getState() != Pitch.State.GOING_SHOOT && pitch.getState()!= Pitch.State.SHOOT) pitch.setState(Pitch.State.GOING_SHOOT);
                if (pitch.getState() == Pitch.State.SHOOT) activeIntake.setState(ActiveIntake.State.SHOOT);
                if (flowerRamp.getState() != FlowerRamp.State.GOING_UP && flowerRamp.getState() != FlowerRamp.State.UP)flowerRamp.setState(FlowerRamp.State.GOING_UP);
                transfer.setState(Transfer.State.SHOOT);
                pitch.setState(Pitch.State.SHOOT);
                break;
            case CONTINUOUS_SHOOT:
                if (pitch.getState() != Pitch.State.GOING_SHOOT && pitch.getState()!= Pitch.State.SHOOT) pitch.setState(Pitch.State.GOING_SHOOT);
                if (pitch.getState() == Pitch.State.SHOOT) activeIntake.setState(ActiveIntake.State.SHOOT);
                if (flowerRamp.getState() != FlowerRamp.State.GOING_UP && flowerRamp.getState() != FlowerRamp.State.UP)flowerRamp.setState(FlowerRamp.State.GOING_UP);
                transfer.setState(Transfer.State.SHOOT);
                pitch.setState(Pitch.State.SHOOT);
                break;
        }
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
}
