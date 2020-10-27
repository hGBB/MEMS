package de.unipa.hams.hamer.aop;

import de.unipa.hams.hamer.crud.service.api.AuditCrudService;
import de.unipa.hams.hamer.crud.service.api.GroupCrudService;
import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.crud.service.api.UserCrudService;
import de.unipa.hams.hamer.domain.Audit;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuditLogger {


  private final AuthorizationService authorizationService;
  private final AuditCrudService auditCrudService;
  private final GroupCrudService groupCrudService;
  private final UserCrudService userCrudService;
  private final NodeCrudService nodeCrudService;

  private void audit(String action, String target) throws AuthorizationContextException {
    User currentUser = authorizationService.currentUser();
    Audit audit = new Audit();
    audit.setActionPerformed(action);
    audit.setTimestamp(new Date());
    audit.setPerformedByName(currentUser.getLogin());
    audit.setTargetName(target);
    auditCrudService.createAudit(audit);
  }

  @AfterReturning(pointcut = "execution(* de.unipa.hams.hamer.crud.service.impl.GroupCrudServiceImpl.createUserGroup(..))",
                  returning="retVal")
  public void auditCreateUG(Object retVal) throws AuthorizationContextException {
    String name = groupCrudService.getUserGroupById((Long) retVal).getName();
    audit("Created UserGroup", name);
  }

  @Before("execution(* de.unipa.hams.hamer.crud.service.impl.GroupCrudServiceImpl.deleteUserGroup(..)) && args(id)")
  public void auditDeleteUG(long id) throws AuthorizationContextException {
   String name = groupCrudService.getUserGroupById(id).getName();
    audit("Deleted UserGroup", name);
  }


  @AfterReturning(pointcut = "execution(* de.unipa.hams.hamer.crud.service.impl.UserCrudServiceImpl.createUser(..))",
                  returning = "retVal")
  public void auditCreateU(Object retVal) throws AuthorizationContextException {
    audit("Created User", ((User)retVal).getLogin());
  }

  @Before("execution(* de.unipa.hams.hamer.crud.service.impl.UserCrudServiceImpl.deleteUser(..)) && args(id)")
  public void auditDeleteU(long id) throws AuthorizationContextException {
    String name = userCrudService.getUser(id).getName();
    audit("Deleted User", name);
  }


  @Before("execution(* de.unipa.hams.hamer.crud.service.impl.NodeCrudServiceImpl.deleteNode(..)) && args(id)")
  public void auditDeleteN(long id) throws AuthorizationContextException {
    String name = nodeCrudService.getNode(id).getName();
    audit("Deleted Node", name);
  }
}
