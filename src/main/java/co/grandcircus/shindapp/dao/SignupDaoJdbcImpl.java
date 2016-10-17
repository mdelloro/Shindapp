package co.grandcircus.shindapp.dao;


import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import co.grandcircus.shindapp.model.Item;
import co.grandcircus.shindapp.model.Signup;
import co.grandcircus.shindapp.model.User;



	@Repository
	@Primary
	
	public class SignupDaoJdbcImpl implements SignupDao{
		private static final Logger logger = LoggerFactory.getLogger (SignupDao.class);

		@Autowired
		JdbcConnectionFactory connectionFactory;

		@Override
		public List<Signup> getAllSignup() {
			String sql = "SELECT * FROM signup";
			try (Connection connection = connectionFactory.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql)) {
				List<Signup> signup = new ArrayList<Signup>();
				while (result.next()) {
					String firstName = result.getString("firstname");
					String  lastName= result.getString("lastname");
					String phoneNumber = result.getString("phonenumber");
					String dishName = result.getString("dishName");
					
					Signup temp = new Signup(firstName,lastName,phoneNumber,dishName);
					temp.setId(result.getInt("id"));		
					signup.add(temp);
					
				}
				return signup;
			} 
			catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
					
					
				
		@Override
		public void addSignup(Signup signup) {
			String sql = "INSERT INTO signup (firstName,lastName,phoneNumber,dishName) VALUES (?, ?,?,?)";
			try (Connection connection = connectionFactory.getConnection();
					PreparedStatement statement = connection.prepareStatement(sql)) {

				
				
				statement.setString(1, signup.getFirstName());
				statement.setString(2, signup.getLastName());
				statement.setString(3, signup.getPhoneNumber());
				statement.setString(4, signup.getDishName());

				int affectedRows = statement.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException("Creating user failed, no rows affected.");
				}

				//try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				//	if (generatedKeys.next()) {
				//		signup.setFirstName(generatedKeys.getString(1));
					//} else {
					//	throw new SQLException("Creating user failed, no ID obtained.");
					//}
				//}

				
			} 
			catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
		@Override	
		public void updateSignup(Signup signup) throws NamingException {
				String sql = "UPDATE signup SET firstname = ?, lastname = ?, phonenumber = ?, dishname = ? WHERE id = ?";
				try (Connection conn = connectionFactory.getConnection();
						PreparedStatement statement = conn
								.prepareStatement(sql)) {
					
					statement.setString(1, signup.getFirstName());
					statement.setString(2, signup.getLastName());
					statement.setString(3, signup.getPhoneNumber());
					statement.setString(4, signup.getDishName());
					statement.setInt(5, signup.getId());

					int rowsUpdated = statement.executeUpdate();
					if (rowsUpdated != 1) {
						throw new NamingException("No name");
					}
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
			@Override
			public Signup getSignup(int id) throws NameNotFoundException {
				String sql = "SELECT * FROM signup WHERE id = ?";
				try (Connection connection = connectionFactory.getConnection();
						PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, id);
					ResultSet result = statement.executeQuery();

					if (result.next()) {
						
						String firstname = result.getString("firstname");
						String lastname = result.getString("lastname");
						String phonenumber = result.getString("phonenumber");
						String dishname = result.getString("dishname");

						Signup temp = new Signup (firstname, lastname, phonenumber, dishname);
						temp.setId(id);
						return temp;
					} else {
						throw new NameNotFoundException("No such user.");
					}
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
			@Override
			public void deleteSignup(int id) throws FileNotFoundException {
				String sql = "DELETE FROM signup WHERE ID = ?";
				try (Connection conn = connectionFactory.getConnection();
						PreparedStatement statement = conn.prepareStatement(sql)) {

					statement.setInt(1, id);

					
					int rowsUpdated = statement.executeUpdate();
					if (rowsUpdated != 1) {
						throw new FileNotFoundException("No such user");
					}
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
}