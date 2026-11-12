package org.firstinspires.ftc.teamcode.LogicNodes;

import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.flower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.gardenPos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.goingFlower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.BlueSoloTrajectory.shootPos;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Components.Intake.Intake;
import org.firstinspires.ftc.teamcode.Components.Intake.Ramp;
import org.firstinspires.ftc.teamcode.Components.Intake.Storage;
import org.firstinspires.ftc.teamcode.Components.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Robot.Node;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

public class BlueAuto {
    boolean hasShot = false;
    ElapsedTime globalTimer; ElapsedTime timer;
    Chassis chassis;
    Shooter shooter;
    Intake intake;
    Odo odo;
    Node shoot,garden,flower1,flower2,goingFlower1,goingFlower2,park;
    public Node currentNode;
    public BlueAuto(HardwareMap hardwareMap){
        Hardware.init(hardwareMap);
        globalTimer = new ElapsedTime(); timer = new ElapsedTime();

        chassis = new Chassis(Chassis.State.PID);
        shooter = new Shooter(Shooter.State.ACTIVE);
        shooter.turret.setState(Turret.State.BLUE);
        odo =new Odo();
        intake = new Intake();

        shoot = new Node("shoot");
        garden = new Node("garden");
        flower1 = new Node("flower1");
        flower2 = new Node("flower2");
        goingFlower1 = new Node("goingFlower1");
        goingFlower2 = new Node("goingFlower2");
        park = new Node("park");

        shoot.addConditions(
                ()->{
                    chassis.setTargetPosition(shootPos[Math.min(shoot.index,shootPos.length-1)]);
                    if (shooter.getState()!= Shooter.State.SHOOT)shooter.setState(Shooter.State.ACTIVE);
                    if (!chassis.inPosition(120,120,0.1)) intake.setState(Intake.State.REVERSE);
                    else if (chassis.inPosition(120,120,0.1) && !hasShot) {
                        intake.setState(Intake.State.SHOOT);
                        shooter.setState(Shooter.State.SHOOT);
                    }
                },
                ()->{
                    return intake.storage.getState() == Storage.State.NO_POLLEN;
                },
                new Node[]{garden,goingFlower1,goingFlower2,park}
        );
        garden.addConditions(
                ()->{
                    chassis.setTargetPosition(gardenPos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(150,150,0.11))Chassis.kp = 0.0015;
                },
                ()->{
                    return chassis.inPosition(60,60,0.1);
                },
                new Node[]{}
        );
        goingFlower1.addConditions(
                ()->{
                    chassis.setTargetPosition(goingFlower1Pos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(150,150,0.11)) intake.deployRamp();
                },
                ()->{
                    return intake.ramp.getState() == Ramp.State.DOWN;
                },
                new Node[]{flower1}
        );
        flower1.addConditions(
                ()->{
                    chassis.setTargetPosition(flower1Pos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(120,120,0.1))Chassis.kp = 0.0015;

                },
                ()->{
                    return true;
                },
                new Node[]{shoot}
        );
    }
}

