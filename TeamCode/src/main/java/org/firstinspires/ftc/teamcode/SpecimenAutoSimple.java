package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunning.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Autonomous
public class SpecimenAutoSimple extends LinearOpMode{
    private MecanumDrive drive;
    DcMotor arm; // motor for arm
    DcMotor slide; // motor for slide
    Servo elbow; // servo for elbow
    Servo claw; // servo for claw

    // for arm and slide
    static final double COUNTS_PER_MOTOR_REV43 = 3895.9; // setting for 43 RPM motor
    static final double COUNTS_PER_MOTOR_REV312 = 3895.9; // setting for 312 RPM motor
    static final int CYCLE_MS = 50; // period of each cycle, set to 50 milliseconds
    int targetPositionArm = 0; // To store the current target position for arm
    int targetPositionSlide = 0; // To store the current position for slide

    // For elbow joint
    static final double INCREMENT_ELBOW = 0.03;     // amount to slew servo each CYCLE_MS cycle
    static double MAX_POS_ELBOW =  1;     // Maximum rotational position
    static double MIN_POS_ELBOW =  0;       // Minimum rotational position
    double positionElbow = 0; // Start at minimum position





    private void sleepTools(int ms) {
        Timing.Timer timer = new Timing.Timer(ms, TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()){


        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        arm = hardwareMap.get(DcMotor.class, "AR");
        slide = hardwareMap.get(DcMotor.class, "SL");
        elbow = hardwareMap.get(Servo.class, "EL");
        claw = hardwareMap.get(Servo.class, "CL");
        claw.setPosition(0.4);


        arm.setDirection(DcMotor.Direction.REVERSE); // Adjust this as needed
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setTargetPosition(0);

        slide.setDirection(DcMotor.Direction.REVERSE); // Adjust this as needed
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setTargetPosition(0);

        final double targetScore = 0.4; // Set appropriately
        final double targetPickUp = 0.625;            // Set appropriately
        final double targetZeroARM = 0;
        final double targetZeroSLIDE = 0;
        //double targetEnter = 0.6;
        final double targetExtend = 0.6;  // Set as needed


        // put in specimen
        waitForStart();
        // move back 0.5 secs
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(-0.2,0),0));
        sleepTools(500);

        // stop robot to lift arm
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0),0));
        sleepTools(2000);
        arm.setTargetPosition((int) (COUNTS_PER_MOTOR_REV43 * targetScore));
        arm.setPower(0.7); // hold
        sleepTools(2000);
        slide.setTargetPosition(0);
        elbow.setPosition(0.3); // hold down
        sleepTools(2000);

        // push back and clip
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(-0.2,0),0));
        sleepTools(1200);
        elbow.setPosition(0.5); //  raise elbow
        sleepTools(1300);

        // open claw
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0),0));
        claw.setPosition(0.6); // open claw
        sleepTools(2000);

        // move back
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0.28,0),0));
        sleepTools(1000);

        // stop to reset arm
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0),0));
        sleepTools(2000);
        arm.setTargetPosition(0); // base pos
        elbow.setPosition(0.1); // safe elbow pos
        sleepTools(2000);

        // move to observation zone
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0.5),0));
        sleepTools(3000);
    }
}
