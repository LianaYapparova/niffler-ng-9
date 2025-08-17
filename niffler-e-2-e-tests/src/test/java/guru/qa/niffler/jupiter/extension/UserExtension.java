package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;

import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;

import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.data.entity.user.CurrencyValues.RUB;
import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import static guru.qa.niffler.utils.RandomDataUtils.*;
import static guru.qa.niffler.utils.RandomDataUtils.randomSurname;

public class UserExtension implements
    BeforeEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  public static final String DEFAULT_PASSWORD = "12345";

  private final UsersClient usersClient = UsersClient.getInstance();
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if ("".equals(userAnno.username())) {
            UserJson created = usersClient.createUser(   new UserJson(null, randomUsername(), DEFAULT_PASSWORD, RUB, randomName(),
                    randomSurname(), null, null, null, null, null, null));
            final List<UserJson> incomes = usersClient.addIncomeInvitation(created, userAnno.incomeInvitations());
            final List<UserJson> outcomes = usersClient.addOutcomeInvitation(created, userAnno.outcomeInvitations());
            final List<UserJson> friends = usersClient.addFriend(created, userAnno.friends());

            TestData testData = new TestData(
                DEFAULT_PASSWORD,
                friends,
                incomes,
                outcomes,
                new ArrayList<>(),
                new ArrayList<>()
            );

            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                created.addTestData(testData)
            );
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
      ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
      ParameterResolutionException {
    return createdUser();
  }

  public static UserJson createdUser() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), UserJson.class);
  }
}
