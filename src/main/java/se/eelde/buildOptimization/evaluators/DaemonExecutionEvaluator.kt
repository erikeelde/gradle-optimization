package se.eelde.buildOptimization.evaluators

import org.gradle.api.internal.project.DefaultProject
import org.gradle.launcher.daemon.server.health.DaemonMemoryStatus
import org.gradle.launcher.daemon.server.scaninfo.DaemonScanInfo

class DaemonExecutionEvaluator(private val project: DefaultProject) {
    private val daemonScanInfo: DaemonScanInfo?
        get() {
            try {
                return project.services.get(DaemonScanInfo::class.java)
            } catch (ignored: Throwable) {
            }
            return null
        }

    val daemonMemoryStatus: DaemonMemoryStatus?
        get() {
            try {
                return project.services.get(DaemonMemoryStatus::class.java)
            } catch (ignored: Throwable) {
            }
            return null
        }

//    private val daemonContext: DaemonContext?
//        get() {
//            try {
//                return project.services.get(DaemonContext::class.java)
//            } catch (ignored: Throwable) {
//            }
//            return null
//        }
//
//    private val daemonRunningStats: DaemonRunningStats?
//        get() {
//            try {
//                return project.services.get(DaemonRunningStats::class.java)
//            } catch (ignored: Throwable) {
//            }
//            return null
//        }

    val isSingleUse: Boolean
        get() {
            return daemonScanInfo == null || daemonScanInfo!!.isSingleUse
        }
}
