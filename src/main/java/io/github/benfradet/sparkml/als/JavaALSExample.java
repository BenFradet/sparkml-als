package io.github.benfradet.sparkml.als;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;

import java.io.Serializable;

public class JavaALSExample {

  public static class Rating implements Serializable {
    private int userId;
    private int movieId;
    private float rating;
    private long timestamp;

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public int getMovieId() {
      return movieId;
    }

    public void setMovieId(int movieId) {
      this.movieId = movieId;
    }

    public float getRating() {
      return rating;
    }

    public void setRating(float rating) {
      this.rating = rating;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public static Rating parseRating(String str) {
      String[] fields = str.split("::");
      assert(fields.length == 4);
      Rating rating = new Rating();
      rating.setUserId(Integer.parseInt(fields[0]));
      rating.setMovieId(Integer.parseInt(fields[1]));
      rating.setRating(Float.parseFloat(fields[2]));
      rating.setTimestamp(Long.parseLong(fields[3]));
      return rating;
    }
  }

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("JavaALSExample");
    JavaSparkContext jsc = new JavaSparkContext(conf);
    SQLContext sqlContext = new SQLContext(jsc);

    JavaRDD<Rating> ratingsRDD =
      jsc.textFile("data/mllib/als/sample_movielens_ratings.txt").map(Rating::parseRating);
    DataFrame ratings = sqlContext.createDataFrame(ratingsRDD, Rating.class);
    DataFrame[] splits = ratings.randomSplit(new double[]{0.8, 0.2});
    DataFrame training = splits[0];
    DataFrame test = splits[1];

    // Build the recommendation model using ALS on the training data
    ALS als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating");
    ALSModel model = als.fit(training);

    // Evaluate the model by computing the RMSE on the test data
    DataFrame rawPredictions = model.transform(test);
    DataFrame predictions = rawPredictions
      .withColumn("rating", rawPredictions.col("rating").cast(DataTypes.DoubleType))
      .withColumn("prediction", rawPredictions.col("prediction").cast(DataTypes.DoubleType));

    RegressionEvaluator evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction");
    Double rmse = evaluator.evaluate(predictions);
    System.out.println("Root-mean-square error = " + rmse);
    jsc.stop();
  }
}
