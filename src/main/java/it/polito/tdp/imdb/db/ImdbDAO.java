package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void loadAllActors(Map <Integer,Actor> idMap){
		String sql = "SELECT * FROM actors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))){

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				idMap.put(actor.getId(), actor); 
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllGenres(){
		String sql = "SELECT DISTINCT(genre) "
				+ "FROM movies_genres "
				+ "ORDER BY genre asc";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getActorsByGenre(Map <Integer,Actor> idMap, String genere){
		String sql = "SELECT DISTINCT(r.actor_id) "
				+ "FROM movies_genres mg, movies m, roles r "
				+ "WHERE mg.movie_id=m.id AND m.id=r.movie_id AND genre=?";
		
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor=idMap.get(res.getInt("r.actor_id")); 
				result.add(actor); 
				

				}
			
			conn.close();
			return result; 
			
		} 	catch (SQLException e) {
			e.printStackTrace();
			return null; 

		}
	}
	
	public List<Arco> getArchi(Map <Integer,Actor> idMap, String genere){
		String sql = "SELECT r1.actor_id, r2.actor_id "
				+ "FROM movies_genres mg, movies m, roles r1, roles r2 "
				+ "WHERE mg.movie_id=m.id AND m.id=r1.movie_id AND genre=? AND m.id=r2.movie_id AND r1.actor_id!=r2.actor_id";
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Arco arco=new Arco((idMap.get(res.getInt("r1.actor_id"))), idMap.get(res.getInt("r2.actor_id"))); 
				result.add(arco); 
				

				}
			
			conn.close();
			return result; 
			
		} 	catch (SQLException e) {
			e.printStackTrace();
			return null; 

		}
	}
	
	
	
	
}
