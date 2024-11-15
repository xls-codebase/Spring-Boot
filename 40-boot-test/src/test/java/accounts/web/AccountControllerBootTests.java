package accounts.web;

import accounts.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import rewards.internal.account.Account;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO-07: Replace @ExtendWith(SpringExtension.class) with the following annotation
// - @WebMvcTest(AccountController.class) // includes @ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerBootTests {

	// TODO-08: Autowire MockMvc bean
	@Autowired
	private MockMvc mockMvc;

	// TODO-09: Create AccountManager mock bean using @MockBean annotation
	@MockBean
	private AccountManager accountManager;

	// TODO-10: Write positive test for GET request for an account
	// - Uncomment the code and run the test and verify it succeeds
	@Test
	public void accountDetails() throws Exception {

		given(accountManager.getAccount(0L))
				.willReturn(new Account("1234567890", "John Doe"));

		mockMvc.perform(get("/accounts/0"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("name").value("John Doe"))
			   .andExpect(jsonPath("number").value("1234567890"));

		verify(accountManager).getAccount(0L);

	}

	// TODO-11: Write negative test for GET request for a non-existent account
	// - Uncomment the "given" and "verify" statements
	// - Write code between the "given" and "verify" statements
	// - Run the test and verify it succeeds
	@Test
	public void accountDetailsFail() throws Exception {

		given(accountManager.getAccount(any(Long.class)))
				.willThrow(new IllegalArgumentException("No such account with id " + 0L));

//		 (Write code here)
//		 - Use mockMvc to perform HTTP Get operation using "/accounts/9999"
//           as a non-existent account URL
//		 - Verify that the HTTP response status is 404
		mockMvc.perform(get("/accounts/9999"))
				.andExpect(status().isNotFound());

		verify(accountManager).getAccount(any(Long.class));

	}

    // TODO-12: Write test for `POST` request for an account
	// - Uncomment Java code below
	// - Write code between the "given" and "verify" statements
	// - Run the test and verify it succeeds
	@Test
	public void createAccount() throws Exception {

		Account testAccount = new Account("1234512345", "Mary Jones");
		testAccount.setEntityId(21L);

		given(accountManager.save(any(Account.class)))
				.willReturn(testAccount);

		// (Write code here)
		// Use mockMvc to perform HTTP Post operation to "/accounts"
		// - Set the request content type to APPLICATION_JSON
		// - Set the request content with Json string of the "testAccount"
		//   (Use "asJsonString" method below to convert the "testAccount"
		//   object into Json string)
		// - Verify that the response status is 201
		// - Verify that the response "Location" header contains "http://localhost/accounts/21"

		mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(testAccount)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/21"));

		verify(accountManager).save(any(Account.class));

	}

//	Extra Part 1
//	Test Get All Accounts
//	1. Add another MockMvc test for getting all accounts. The test should verify the following:
//	- The returned Http status is 200
//	- The returned content type is JSON (MediaType.APPLICATION_JSON)
//	- The returned data contains the correct name, number, and contains the correct number of accounts
//    (at least 21 - maybe more due to the POST test creating new accounts)
	@Test
	public void getAllAccounts() throws Exception {
		List<Account> accounts = List.of(
		new Account("123456789", "Keith and Keri Donald"),
		new Account("123456001", "Dollie R. Adams"),
		new Account("123456002", "Cornelia J. Andresen"),
		new Account("123456003", "Coral Villareal Betancourt"),
		new Account("123456004", "Chad I. Cobbs"),
		new Account("123456005", "Michael C. Feller"),
		new Account("123456006", "Michael J. Grover"),
		new Account("123456007", "John C. Howard"),
		new Account("123456008", "Ida Ketterer"),
		new Account("123456009", "Laina Ochoa Lucero"),
		new Account("123456010", "Wesley M. Mayo"),
		new Account("123456011", "Leslie F. Mcclary"),
		new Account("123456012", "John D. Mudra"),
		new Account("123456013", "Pietronella J. Nielsen"),
		new Account("123456014", "John S. Oleary"),
		new Account("123456015", "Glenda D. Smith"),
		new Account("123456016", "Willemina O. Thygesen"),
		new Account("123456017", "Antje Vogt"),
		new Account("123456018", "Julia Weber"),
		new Account("123456019", "Mark T. Williams"),
		new Account("123456020", "Christine J. Wilson")
		);

		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).setEntityId((long) i);
		}

		given(accountManager.getAllAccounts())
				.willReturn(accounts);

		MvcResult mvcResult = mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.*", hasSize(accounts.size()) ))
				.andReturn();

		String response = mvcResult.getResponse().getContentAsString();

		for (int i = 0; i < accounts.size(); i++) {
			assertEquals(accounts.get(i).getNumber(), JsonPath.parse(response).read("$[" + i + "].number"));
			assertEquals(accounts.get(i).getName(), JsonPath.parse(response).read("$[" + i + "].name"));
		}

		verify(accountManager).getAllAccounts();
	}


    // Utility class for converting an object into JSON string
	protected static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// TODO-13 (Optional): Experiment with @MockBean vs @Mock
	// - Change `@MockBean` to `@Mock` for the `AccountManager dependency above
	// - Run the test and observe a test failure
	// - Change it back to `@MockBean`

}
