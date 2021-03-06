package de.reffle.jfsdict.util;


public class Stopwatch {

  long startNanos;

  public Stopwatch() {
    reset();
  }

  public void reset() {
    startNanos = now();
  }

  public long getMillis() {
    return getNanos() / 1000000;
  }

  private long now() {
    return System.nanoTime();
  }

  public long getNanos() {
    return now() - startNanos;
  }


}
