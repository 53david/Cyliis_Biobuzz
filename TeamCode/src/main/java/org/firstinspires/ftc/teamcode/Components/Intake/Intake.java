package org.firstinspires.ftc.teamcode.Components.Intake;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    ElapsedTime timer;
    public ActiveIntake activeIntake;
    public Storage storage;
    public Transfer transfer;
    public enum State{
        IDLE,
        REVERSE,
        ACTIVE,
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
        storage = new Storage();
        transfer = new Transfer();
    }
    public void update(){
        stateUpdate();
        activeIntake.update();
        storage.update();
        transfer.update();

        if (state != State.SHOOT) timer.reset();
    }
    public void stateUpdate(){
        switch (state){
            case IDLE:
                activeIntake.setState(ActiveIntake.State.IDLE);
                storage.setState(Storage.State.IDLE);
                transfer.setState(Transfer.State.IDLE);
                break;
            case ACTIVE:
                activeIntake.setState(ActiveIntake.State.ACTIVE);
                storage.setState(Storage.State.ACTIVE);
                transfer.setState(Transfer.State.IDLE);
                break;
            case REVERSE:
                activeIntake.setState(ActiveIntake.State.REVERSE);
                storage.setState(Storage.State.IDLE);
                transfer.setState(Transfer.State.IDLE);
                break;
            case SHOOT:
                if (timer.seconds()>0.5) state = state.nextState;
                activeIntake.setState(ActiveIntake.State.SHOOT);
                storage.setState(Storage.State.SHOOT);
                transfer.setState(Transfer.State.SHOOT);
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
