package net.ornithemc.osl.crashreports.api.crash;

import java.util.concurrent.Callable;

public interface CrashReportDetail<V> extends Callable<V> {
}
