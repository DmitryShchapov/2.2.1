package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public void cleanUsersTable() {
      sessionFactory.getCurrentSession().createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
   }

   @Override
   public User getUserByCar(String model, int series) {
      TypedQuery<Car> query1 = sessionFactory.getCurrentSession().createQuery("from Car where model=:model and series=:series");
      query1.setParameter("model", model);
      query1.setParameter("series", series);

//сделал так, что бы не было ошибки, если модель и серия совпадают
      Car car = query1.getResultList().iterator().next();

      TypedQuery<User> query2 = sessionFactory.getCurrentSession().createQuery("from User where car=:car");
      query2.setParameter("car", car);

      return query2.getSingleResult();
   }
}
