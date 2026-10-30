package org.firstinspires.ftc.teamcode.LogicNodes;

import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.flower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.flower2Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.gardenPos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.goingFlower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.goingFlower2Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.parkPos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.shootPos;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Components.Intake.Ramp;
import org.firstinspires.ftc.teamcode.Components.Intake.Intake;
import org.firstinspires.ftc.teamcode.Components.Intake.Storage;
import org.firstinspires.ftc.teamcode.Components.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Robot.Node;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Limelight;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
@Config
public class BlueAuto {
    ElapsedTime timer, globalTimer;
    Chassis chassis;
    Intake intake;
    Shooter shooter;
    Limelight limelight;
    Odo odo;
    Node shoot, goingFlower1, goingFlower2, flower1, flower2, garden, park;
    public Node currentNode;

    public BlueAuto(HardwareMap hardwareMap) {
        Hardware.init(hardwareMap);
        timer = new ElapsedTime();
        globalTimer = new ElapsedTime();
        timer.startTime();
        globalTimer.startTime();
        odo = new Odo();
        chassis = new Chassis(Chassis.State.PID);
        intake = new Intake();
        shooter = new Shooter(Shooter.State.ACTIVE);
        limelight = new Limelight(Limelight.State.STOP);
        shoot = new Node("shoot");
        goingFlower1 = new Node("goingFlower1");
        goingFlower2 = new Node("goingFlower2");
        flower1 = new Node("flower1");
        flower2 = new Node("flower2");
        garden = new Node("garden");
        park = new Node("park");
        currentNode = shoot;

        shoot.addConditions(
                () -> {
                    intake.retractRamp();
                    if (limelight.getHivePosition() == Limelight.HivePosition.UP) chassis.setTargetPosition(shootPos[0]);
                    else chassis.setTargetPosition(shootPos[1]);
                    if (intake.storage.getState() == Storage.State.READY_FOR_TRANSFER && intake.getState()!= Intake.State.SHOOT) {intake.setState(Intake.State.REVERSE);}
                    else if (intake.storage.getState() == Storage.State.WAITING_FOR_POLLEN) {intake.setState(Intake.State.IDLE);}
                    if (chassis.inPosition(120, 120, 0.1)) {intake.setState(Intake.State.SHOOT);shooter.setState(Shooter.State.SHOOT);}
                },
                () -> {
                    return intake.storage.getState() == Storage.State.NO_POLLEN;
                },
                new Node[]{garden,goingFlower2,goingFlower1}
        );
        garden.addConditions(
                ()->{
                    intake.retractRamp();
                    chassis.setTargetPosition(gardenPos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(110,110,0.1))chassis.setPIDCoefficients(0.0065,0,0);
                    else timer.reset();
                    },
                ()->{
                    return timer.seconds()>1.5 || intake.storage.getState() == Storage.State.READY_FOR_TRANSFER;
                },
                new Node[]{shoot}

        );
        goingFlower1.addConditions(
                () -> {
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(goingFlower1Pos);
                    if (chassis.inPosition(220,220,0.1)) intake.deployRamp();
                },
                ()->{
                    return intake.ramp.getState() == Ramp.State.DOWN;
                },
                new Node[]{flower1}
                );
        flower1.addConditions(
                ()->{
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(flower1Pos); intake.deployRamp();
                    if (chassis.inPosition(85,85,0.1))chassis.setPIDCoefficients(0.0065,0,0);
                    else timer.reset();
                    },
                ()->{
                    return intake.storage.getState()== Storage.State.READY_FOR_TRANSFER;
                },
                new Node[]{shoot}
        );
        goingFlower2.addConditions(
                () -> {
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(goingFlower2Pos,false);
                    if (chassis.inPosition(220,220,0.1)) intake.deployRamp();
                },
                ()->{
                    return intake.ramp.getState() == Ramp.State.DOWN;
                },
                new Node[]{flower2}
        );
        flower2.addConditions(
                ()->{
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(flower2Pos);
                    intake.deployRamp();
                    if (chassis.inPosition(110,110,0.1))chassis.setPIDCoefficients(0.0065,0,0);
                    else timer.reset();
                },
                ()->{
                    return intake.storage.getState()== Storage.State.READY_FOR_TRANSFER;
                },
                new Node[]{shoot}
        );
        park.addConditions(
                ()->{
                    chassis.setTargetPosition(parkPos);
                    intake.setState(Intake.State.IDLE);
                    shooter.setState(Shooter.State.IDLE);
                },
                ()->{
                    return chassis.inPosition(30,30,0.1);
                },
                new Node[]{park}
        );

    }
    public void update(){
        currentNode.run();
        chassis.update();
        intake.update();
        shooter.update();
        odo.update();

        if (currentNode != flower1 && currentNode != flower2) chassis.turnOn();
        if (currentNode != garden) chassis.setPIDCoefficients(0.0065,0,0);
        if (currentNode != shoot && globalTimer.seconds()>29) currentNode = park;
        if (currentNode.transition()){
            currentNode = currentNode.next[Math.min(currentNode.index++, currentNode.next.length-1)];
        }
    }
}

