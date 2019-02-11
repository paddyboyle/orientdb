package com.orientechnologies.orient.server.distributed.impl;

import com.orientechnologies.orient.core.command.OCommandDistributedReplicateRequest;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.distributed.ODistributedRequestId;
import com.orientechnologies.orient.server.distributed.ODistributedServerManager;
import com.orientechnologies.orient.server.distributed.task.OAbstractRemoteTask;
import com.orientechnologies.orient.server.distributed.task.ORemoteTask;

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class OWaitPartitionsReadyTask extends OAbstractRemoteTask {
  private volatile int      waitCount;
  private final    Runnable toRun;

  public OWaitPartitionsReadyTask(int waitCount, Runnable runnable) {
    this.waitCount = waitCount;
    this.toRun = runnable;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public OCommandDistributedReplicateRequest.QUORUM_TYPE getQuorumType() {
    return null;
  }

  @Override
  public Object execute(ODistributedRequestId requestId, OServer iServer, ODistributedServerManager iManager,
      ODatabaseDocumentInternal database) throws Exception {
    synchronized (this) {
      waitCount--;
      if (waitCount == 0) {
        toRun.run();
      }
    }
    return null;
  }

  @Override
  public int getFactoryId() {
    return 0;
  }

  @Override
  public boolean isUsingDatabase() {
    return false;
  }

}
