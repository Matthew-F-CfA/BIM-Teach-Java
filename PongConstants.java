class PongConstants {
  volatile static Object syncAll=new Object();

  final static int USER_NAME_ACCEPTED=0;
  final static int USER_NAME_REJECTED=1;

  final static int REQUEST_CHALLENGE_ACCEPTED=0;
  final static int REQUEST_CHALLENGE_REJECTED=1;

//Server message constants
  final static int NEW_USER=0;
  final static int REMOVE_USER=1;
  final static int REPAINT_BALL=2;
  final static int REPAINT_LEFT_SLIDER=3;
  final static int REPAINT_RIGHT_SLIDER=4;
  final static int ANNOUNCE_WINNER=5;
  final static int CHALLENGE_INITIATED=6;

//Client message constants
  final static int MOVE_SLIDER_UP=0;
  final static int MOVE_SLIDER_DOWN=1;
  final static int CLOSE_CONNECTION=2;
  final static int REQUEST_CHALLENGE=3;
  final static int JOIN_QUEUE=4;
}