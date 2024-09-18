package com.venzee.chat_app_demon.utility;

import java.time.Instant;

public record Message(User user, String comment, Action action, Instant timeStamp){

}
