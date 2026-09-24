package org.firstinspires.ftc.teamcode.Wrappers;

import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Limelight {
    Limelight3A limelight;
    public boolean stop = false;
    public int index,prevIndex;
    public enum State{
        ACTIVE,
        STOP,
    };
    public State state;
    public double posX = 0,posY = 0, posZ = 0;
    public Limelight(State state){
        limelight = Hardware.limelight;
        this.state = state;

    }
    public void update(){
        updateState();
        updateHardware();
    }
    private void updateState(){
        switch (state){
            case STOP:
                if (limelight.isRunning()) {limelight.shutdown();}
                break;
            case ACTIVE:
                if (!limelight.isRunning()) {limelight.start();}
                break;
        }
    }
    private void updateHardware(){
        if (state == State.STOP) return;
        limelight.updateRobotOrientation(Odo.heading);
        setPipelineIndex(index);
        if (limelight.getLatestResult()!=null && limelight.getLatestResult().isValid() && index == 0){
            posX = limelight.getLatestResult().getBotpose_MT2().getPosition().x;
            posY = limelight.getLatestResult().getBotpose_MT2().getPosition().y;
            posZ = limelight.getLatestResult().getBotpose_MT2().getPosition().z;
        }
        prevIndex = index;

    }
    public double getPosX(){
        return posX;
    }
    public double getPosY(){
        return posY;
    }
    public double getPosZ(){
        return posZ;
    }
    public void setPipelineIndex(int index){
        if (prevIndex != index) this.index =index;
    }
    public void setState(State state){
        this.state = state;
    }
}
