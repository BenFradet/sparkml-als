from __future__ import print_function

from pyspark import SparkContext
from pyspark.ml.evaluation import RegressionEvaluator
from pyspark.ml.recommendation import ALS
from pyspark.sql import Row, SQLContext

if __name__ == "__main__":
    sc = SparkContext(appName="ALSExample")
    logger = sc._jvm.org.apache.log4j
    logger.LogManager.getLogger("org").setLevel(logger.Level.WARN)
    sqlContext = SQLContext(sc)

    lines = sc.textFile("data/sample_movielens_ratings.txt")
    parts = lines.map(lambda l: l.split("::"))
    ratingsRDD = parts.map(lambda p: Row(userId=int(p[0]), movieId=int(p[1]),
                                         rating=float(p[2]), timestamp=long(p[3])))
    ratings = sqlContext.createDataFrame(ratingsRDD)
    (training, test) = ratings.randomSplit([0.8, 0.2])

    # Build the recommendation model using ALS on the training data
    als = ALS(maxIter=5, regParam=0.01, userCol="userId", itemCol="movieId", ratingCol="rating")
    model = als.fit(training)

    # Evaluate the model by computing the RMSE on the test data
    rawPredictions = model.transform(test)
    predictions = rawPredictions\
        .withColumn("rating", rawPredictions.rating.cast("double"))\
        .withColumn("prediction", rawPredictions.prediction.cast("double"))
    evaluator =\
        RegressionEvaluator(metricName="rmse", labelCol="rating", predictionCol="prediction")
    rmse = evaluator.evaluate(predictions)
    print("Root-mean-square error = " + str(rmse))
    sc.stop()
